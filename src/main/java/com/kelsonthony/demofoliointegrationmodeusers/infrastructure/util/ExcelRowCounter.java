package com.kelsonthony.demofoliointegrationmodeusers.infrastructure.util;

import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.stereotype.Component;

import java.io.FileInputStream;
import java.io.IOException;

@Component
public class ExcelRowCounter {

    public int countRows(String filePath) {
        try {
            FileInputStream file = new FileInputStream(filePath);
            Sheet sheet = WorkbookFactory.create(file).getSheetAt(0);
            return sheet.getLastRowNum() + 1;  // Conta o número total de linhas no arquivo Excel
        } catch (IOException e) {
            throw new RuntimeException("Erro ao contar linhas no arquivo Excel", e);
        }
    }
}