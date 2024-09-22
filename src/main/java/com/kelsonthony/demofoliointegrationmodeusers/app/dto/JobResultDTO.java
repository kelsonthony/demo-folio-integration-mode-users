package com.kelsonthony.demofoliointegrationmodeusers.app.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class JobResultDTO {
    private Long executionId;
    private Map<Long, StepExecutionDTO> results;
}