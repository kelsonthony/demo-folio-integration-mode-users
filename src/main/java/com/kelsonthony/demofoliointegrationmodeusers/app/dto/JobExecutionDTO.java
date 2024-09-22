package com.kelsonthony.demofoliointegrationmodeusers.app.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Data
public class JobExecutionDTO {
    private Long id;
    private String status;
    private String exitStatus;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private List<StepExecutionDTO> stepExecutions;
    private Map<String, Object> jobParameters;

}