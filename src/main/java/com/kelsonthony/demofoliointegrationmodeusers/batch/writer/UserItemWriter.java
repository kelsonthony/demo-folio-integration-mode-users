package com.kelsonthony.demofoliointegrationmodeusers.batch.writer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.util.StdDateFormat;
import com.kelsonthony.demofoliointegrationmodeusers.app.dto.UserDTO;
import com.kelsonthony.demofoliointegrationmodeusers.folioclient.FolioClient;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;


@Component
public class UserItemWriter implements ItemWriter<UserDTO> {

    private final WebClient webClient;
    private final FolioClient folioClient;
    private final ObjectMapper objectMapper;
    private final List<UserDTO> failedUsers = new ArrayList<>();

    private static final int MAX_RETRIES = 3;

    // Injeta o caminho do arquivo a partir do application.properties
    @Value("${report.output.filepath}")
    private String reportFilePath;

    public UserItemWriter(WebClient.Builder webClientBuilder, FolioClient folioClient) {
        this.webClient = webClientBuilder.build();
        this.folioClient = folioClient;
        this.objectMapper = new ObjectMapper();
        this.objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        this.objectMapper.setDateFormat(new StdDateFormat().withColonInTimeZone(true)); // ISO 8601
    }

    @Override
    public void write(@NonNull Chunk<? extends UserDTO> users) {
        users.forEach(user -> processUserWithRetries(user, 0));
        if (!failedUsers.isEmpty()) {
            generateFailedUsersReport();
        }
    }

    private void processUserWithRetries(UserDTO user, int attempt) {
        sendUserToApi(user)
                .doOnSuccess(response -> {
                    System.out.println("Successfully sent user: " + user.getUsername());
                    System.out.println("API Response: " + response);
                })
                .doOnError(error -> {
                    System.err.println("Error sending user: " + user.getUsername() + " - " + error.getMessage());
                    if (attempt < MAX_RETRIES) {
                        System.out.println("Retrying user: " + user.getUsername() + " (Attempt " + (attempt + 1) + ")");
                        processUserWithRetries(user, attempt + 1); // Retry
                    } else {
                        System.err.println("Failed to send user after " + MAX_RETRIES + " attempts: " + user.getUsername());
                        failedUsers.add(user); // Add to failed list
                    }
                })
                .subscribe();
    }

    private Mono<UserDTO> sendUserToApi(UserDTO user) {
        try {
            System.out.println("Sending user to API: " + user);
            String json = objectMapper.writeValueAsString(user); // Use ISO format for dates
            System.out.println("Sending JSON: " + json);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return webClient.post()
                .uri(folioClient.getApiBaseUrl() + "/users")
                .header("x-okapi-token", folioClient.getApiToken())
                .header("Content-Type", "application/json")
                .bodyValue(user)
                .retrieve()
                .onStatus(
                        status -> status.value() == 400 || status.value() == 422,
                        response -> response.bodyToMono(String.class).flatMap(body -> {
                            System.err.println("Error " + response.statusCode() + " - Bad Request: " + body);
                            return Mono.error(new RuntimeException("Bad Request: " + body));
                        })
                )
                .bodyToMono(UserDTO.class);
    }

    private void generateFailedUsersReport() {
        String[] columns = {"active", "firstName", "middleName", "lastName", "preferredFirstName", "dateOfBirth", "email",
                "phone", "mobilePhone", "addressLine1", "addressLine2", "city", "region", "postalCode",
                "addressTypeId", "countryId", "preferredContactTypeId", "username", "patronGroup",
                "expirationDate", "barcode", "enrollmentDate", "externalSystemId", "departments"};

        try (Workbook workbook = new XSSFWorkbook(); FileOutputStream fileOut = new FileOutputStream(reportFilePath)) {
            Sheet sheet = workbook.createSheet("Failed Users");

            // Create header row
            Row headerRow = sheet.createRow(0);
            for (int i = 0; i < columns.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(columns[i]);
            }

            // Create rows for failed users
            int rowNum = 1;
            for (UserDTO user : failedUsers) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(user.isActive());
                row.createCell(1).setCellValue(user.getPersonal().getFirstName());
                row.createCell(2).setCellValue(user.getPersonal().getMiddleName());
                row.createCell(3).setCellValue(user.getPersonal().getLastName());
                row.createCell(4).setCellValue(user.getPersonal().getPreferredFirstName());
                row.createCell(5).setCellValue(user.getPersonal().getDateOfBirth().toString());
                row.createCell(6).setCellValue(user.getPersonal().getEmail());
                row.createCell(7).setCellValue(user.getPersonal().getPhone());
                row.createCell(8).setCellValue(user.getPersonal().getMobilePhone());
                row.createCell(9).setCellValue(user.getPersonal().getAddresses().get(0).getAddressLine1());
                row.createCell(10).setCellValue(user.getPersonal().getAddresses().get(0).getAddressLine2());
                row.createCell(11).setCellValue(user.getPersonal().getAddresses().get(0).getCity());
                row.createCell(12).setCellValue(user.getPersonal().getAddresses().get(0).getRegion());
                row.createCell(13).setCellValue(user.getPersonal().getAddresses().get(0).getPostalCode());
                row.createCell(14).setCellValue(user.getPersonal().getAddresses().get(0).getAddressTypeId());
                row.createCell(15).setCellValue(user.getPersonal().getAddresses().get(0).getCountryId());
                row.createCell(16).setCellValue(user.getPersonal().getPreferredContactTypeId());
                row.createCell(17).setCellValue(user.getUsername());
                row.createCell(18).setCellValue(user.getPatronGroup());
                row.createCell(19).setCellValue(user.getExpirationDate().toString());
                row.createCell(20).setCellValue(user.getBarcode());
                row.createCell(21).setCellValue(user.getEnrollmentDate().toString());
                row.createCell(22).setCellValue(user.getExternalSystemId());
                row.createCell(23).setCellValue(String.join(", ", user.getDepartments()));
            }

            workbook.write(fileOut);
            System.out.println("Failed users report generated: " + reportFilePath);
        } catch (IOException e) {
            System.err.println("Error generating Excel report for failed users: " + e.getMessage());
        }
    }
}

