package com.kelsonthony.demofoliointegrationmodeusers.batch.writer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kelsonthony.demofoliointegrationmodeusers.app.dto.UserDTO;
import com.kelsonthony.demofoliointegrationmodeusers.folioclient.FolioClient;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
public class UserItemWriter implements ItemWriter<UserDTO> {

    private final WebClient webClient;
    private final FolioClient folioClient;

    public UserItemWriter(WebClient.Builder webClientBuilder, FolioClient folioClient) {
        this.webClient = webClientBuilder.build();
        this.folioClient = folioClient;
    }

    @Override
    public void write(@NonNull Chunk<? extends UserDTO> users) {
        users.forEach(user -> sendUserToApi(user)
                .doOnSuccess(response -> {
                    System.out.println("Successfully sent user: " + user.getUsername());
                    System.out.println("API Response: " + response);
                })
                .doOnError(error -> System.err.println("Error sending user: "
                        + user.getUsername() + " - " + error.getMessage()))
                .subscribe()
        );
    }

    private Mono<UserDTO> sendUserToApi(UserDTO user) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            System.out.println("Sending user to API: " + user);
            String json = objectMapper.writeValueAsString(user);
            System.out.println("Sending JSON: " + json); // Verificar o JSON final gerado
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
                        status -> status.value() == 400,
                        response -> response.bodyToMono(String.class).flatMap(body -> {
                            System.err.println("Error 400 - Bad Request: " + body);
                            return Mono.error(new RuntimeException("Bad Request: " + body));
                        })
                )
                .bodyToMono(UserDTO.class);
    }


}
