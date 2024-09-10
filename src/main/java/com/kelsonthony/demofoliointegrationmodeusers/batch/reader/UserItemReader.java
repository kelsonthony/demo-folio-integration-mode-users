package com.kelsonthony.demofoliointegrationmodeusers.batch.reader;

import com.kelsonthony.demofoliointegrationmodeusers.app.dto.UserDTO;

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
public class UserItemReader implements ItemReader<UserDTO> {

    private static final Logger logger = LoggerFactory.getLogger(UserItemReader.class);

    private int currentRow = 2;
    private int maxRow;
    private Sheet sheet;

    @Value("#{stepExecutionContext['minValue']}")
    private int minValue;

    @Value("#{stepExecutionContext['maxValue']}")
    private int maxValue;

    @Value("${report.input.filepath}")
    private String inputFilepath;

    private final CellMapper<UserDTO, Void, Integer> cellMapper;

    public UserItemReader(CellMapper<UserDTO, Void, Integer> cellMapper) {
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
    public UserDTO read() {
        int positionColumnIndex = 0;

        // Pular todas as linhas antes da linha 6
        if (currentRow < 2) {
            currentRow = 2;  // Força começar a leitura da linha 6
        }

        if (currentRow > sheet.getLastRowNum()) {
            logger.info("Fim da leitura: Nenhuma linha mais encontrada.");
            return null;
        }

        Row row = sheet.getRow(currentRow++);
        if (row == null || ExcelUtil.isRowEmpty(row)) {
            return read();  // Pula linhas vazias
        }

        UserDTO userDTO = new UserDTO();

        logger.debug("Processando a linha {}", currentRow - 1);

//        IntStream.range(0, 39)
//                .forEach(i -> {
//                    Cell cell = row.getCell(i, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
//                    String cellValue = ExcelUtil.getCellValueAsString(cell).trim();  // Aplica trim() para remover espaços no início e no fim
//                    logger.debug("Coluna {}: valor = {}", i, cellValue);
//
//                    // Verifica se estamos na coluna de 'position'
//                    if (i == positionColumnIndex) {  // Ajuste para o índice correto da coluna 'position'
//                        String positionValue = cellValue;
//                        if (!positionValue.isEmpty()) {
//                            userDTO.setPosition(EnumUtil.getPositionEnum(positionValue));
//                        }
//                    }
//
//                    // Continua o mapeamento de outras colunas no DTO, já com o valor tratado
//                    cellMapper.map(userDTO, i, cellValue);
//                });

        System.out.println("userDTO reader: " + userDTO);
        return userDTO;
    }




}