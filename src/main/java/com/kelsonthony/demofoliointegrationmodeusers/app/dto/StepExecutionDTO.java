package com.kelsonthony.demofoliointegrationmodeusers.app.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StepExecutionDTO {
    private Long id;
    private String name;
    private String status;
    private String exitStatus;
    private long readCount;
    private long filterCount;
    private long writeCount;
    private long readSkipCount;
    private long writeSkipCount;
    private long processSkipCount;
    private long commitCount;
    private long rollbackCount;
}