package com.kelsonthony.demofoliointegrationmodeusers.infrastructure.partition;

import org.springframework.batch.core.partition.support.Partitioner;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.lang.NonNull;

import java.util.HashMap;
import java.util.Map;

public class ColumnRangePartitioner implements Partitioner {

    private final int totalRows;

    public ColumnRangePartitioner(int totalRows) {
        this.totalRows = totalRows;
    }

    @Override
    @NonNull
    public Map<String, ExecutionContext> partition(@NonNull int gridSize) {
        int min = 1;
        int max = totalRows;
        int targetSize = (max - min + 1) / gridSize;

        System.out.println("targetSize: " + targetSize);
        Map<String, ExecutionContext> result = new HashMap<>();

        int number = 0;
        int start = min;
        int end = start + targetSize - 1;

        while (start <= max) {
            ExecutionContext value = new ExecutionContext();
            result.put("partition" + number, value);

            if (end >= max) {
                end = max;
            }
            value.putInt("minValue", start);
            value.putInt("maxValue", end);

            start = end + 1;
            end = start + targetSize - 1;
            number++;
        }

        return result;
    }
}
