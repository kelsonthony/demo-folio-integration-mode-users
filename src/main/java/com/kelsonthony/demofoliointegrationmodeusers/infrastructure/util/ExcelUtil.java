package com.kelsonthony.demofoliointegrationmodeusers.infrastructure.util;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.DecimalFormat;
import java.util.stream.IntStream;

public class ExcelUtil {

    private static final Logger logger = LoggerFactory.getLogger(ExcelUtil.class);

    public static String getCellValueAsString(Cell cell) {
        if (cell == null) {
            return "";
        }

        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue().replace("\n", " ");  // Substitui quebras de linha por espaço
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    return cell.getDateCellValue().toString();
                } else {
                    return new DecimalFormat("#").format(cell.getNumericCellValue());
                }
            case BOOLEAN:
                return Boolean.toString(cell.getBooleanCellValue());
            case FORMULA:
                try {
                    return cell.getStringCellValue();  // Força a leitura do resultado da fórmula como string
                } catch (IllegalStateException e) {
                    return String.valueOf(cell.getNumericCellValue());
                }
            case BLANK:
                return "";
            default:
                logger.warn("Tipo de célula desconhecido: {}", cell.getCellType());
                return "";
        }
    }

    public static String formatarDetailPosition(String valorOriginal) {
        // Exemplo: Adiciona um texto fixo ou concatena partes dinâmicas
        return valorOriginal
                .replaceAll(",", ", ")
                .replaceAll("-", " - ")
                .replaceAll("/", " / ");
    }


    public static boolean isRowEmpty(Row row) {
        return IntStream.range(row.getFirstCellNum(), row.getLastCellNum())
                .mapToObj(row::getCell)
                .noneMatch(cell -> cell != null && cell.getCellType() != CellType.BLANK);
    }


}