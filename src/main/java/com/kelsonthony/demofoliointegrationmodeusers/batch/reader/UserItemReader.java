package com.kelsonthony.demofoliointegrationmodeusers.batch.reader;

import com.kelsonthony.demofoliointegrationmodeusers.app.dto.UserDTO;

import com.kelsonthony.demofoliointegrationmodeusers.app.dto.UserExcelDTO;
import com.kelsonthony.demofoliointegrationmodeusers.infrastructure.mapper.CellMapper;
import com.kelsonthony.demofoliointegrationmodeusers.infrastructure.util.ExcelUtil;
import org.apache.poi.ss.usermodel.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemReader;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.stream.IntStream;

@Component
@Scope(value = "step", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class UserItemReader implements ItemReader<UserExcelDTO> {

    private static final Logger logger = LoggerFactory.getLogger(UserItemReader.class);

    private int currentRow = 1;
    private int maxRow;
    private Sheet sheet;

    @Value("#{stepExecutionContext['minValue']}")
    private int minValue;

    @Value("#{stepExecutionContext['maxValue']}")
    private int maxValue;

    @Value("${report.input.filepath}")
    private String inputFilepath;

    private final CellMapper<UserExcelDTO, Void, Integer> cellMapper;

    public UserItemReader(CellMapper<UserExcelDTO, Void, Integer> cellMapper) {
        this.cellMapper = cellMapper;
    }

    @PostConstruct
    public void startReader() {
        logger.info("Iniciando o leitor para o arquivo: {}", inputFilepath);
        try {
            FileInputStream file = new FileInputStream(inputFilepath);
            Workbook workbook = WorkbookFactory.create(file);
            sheet = workbook.getSheetAt(0);
            currentRow = minValue;  // Inicializa com o valor mínimo da partição
            maxRow = maxValue;  // Define o valor máximo da partição
            logger.info("Arquivo Excel carregado com sucesso: {}", inputFilepath);
        } catch (IOException e) {
            logger.error("Erro ao carregar o arquivo Excel: {}", inputFilepath, e);
            throw new RuntimeException("Failed to read Excel file", e);
        }
    }

    @Override
    public UserExcelDTO read() {
        // Start reading from the second row (headers are on the first row)
        if (currentRow < 2) {
            currentRow = 2;
        }

        if (currentRow > sheet.getLastRowNum()) {
            logger.info("End of file: No more rows to read.");
            return null;
        }

        Row row = sheet.getRow(currentRow++);
        if (row == null || ExcelUtil.isRowEmpty(row)) {
            return read();  // Skip empty rows
        }

        UserExcelDTO userExcelDTO = new UserExcelDTO();

        logger.debug("Processing row {}", currentRow - 1);

        // Map each column using the CellMapper
        IntStream.range(0, 24).forEach(i -> {
            Cell cell = row.getCell(i, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
            String cellValue = ExcelUtil.getCellValueAsString(cell).trim();  // Trim spaces

            logger.debug("Column {}: value = {}", i, cellValue);

            // Map the value from the cell to the appropriate field in UserExcelDTO using CellMapper
            cellMapper.map(userExcelDTO, i, cellValue);
        });

        logger.debug("Mapped UserExcelDTO: {}", userExcelDTO);
        System.out.println("my userExcelDTO reader: " + userExcelDTO);
        return userExcelDTO;
    }
}
