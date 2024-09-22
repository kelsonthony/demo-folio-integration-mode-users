package com.kelsonthony.demofoliointegrationmodeusers.app.api.controller;

import com.kelsonthony.demofoliointegrationmodeusers.app.api.openapi.UserControllerOpenApi;
import com.kelsonthony.demofoliointegrationmodeusers.app.dto.JobExecutionDTO;
import com.kelsonthony.demofoliointegrationmodeusers.app.dto.StepExecutionDTO;
import com.kelsonthony.demofoliointegrationmodeusers.infrastructure.listener.CustomJobExecutionListener;
import org.springframework.batch.core.*;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/users")
public class UserController implements UserControllerOpenApi {

    private final JobLauncher jobLauncher;
    private final Job job;
    private final CustomJobExecutionListener customJobExecutionListener;



    public UserController(JobLauncher jobLauncher,
                          Job job,
                          CustomJobExecutionListener customJobExecutionListener) {
        this.jobLauncher = jobLauncher;
        this.job = job;
        this.customJobExecutionListener = customJobExecutionListener;

    }

    @PostMapping(path = "/importUsers")
    public ResponseEntity<JobExecutionDTO> startBatch() {
        JobParameters jobParameters = new JobParametersBuilder()
                .addLong("startAt", System.currentTimeMillis())
                .addString("runId", UUID.randomUUID().toString())
                .toJobParameters();

        try {
            JobExecution jobExecution = jobLauncher.run(job, jobParameters);

            while (jobExecution.isRunning()) {
                Thread.sleep(1000);
            }

            JobExecutionDTO jobExecutionDTO = new JobExecutionDTO();
            jobExecutionDTO.setId(jobExecution.getId());
            jobExecutionDTO.setStatus(jobExecution.getStatus().toString());
            jobExecutionDTO.setExitStatus(jobExecution.getExitStatus().getExitCode());
            jobExecutionDTO.setStartTime(jobExecution.getStartTime());
            jobExecutionDTO.setEndTime(jobExecution.getEndTime());

            Map<String, Object> jobParamsMap = jobExecution.getJobParameters()
                    .getParameters().entrySet().stream()
                    .collect(Collectors.toMap(
                            Map.Entry::getKey,
                            entry -> entry.getValue().getValue()
                    ));
            jobExecutionDTO.setJobParameters(jobParamsMap);

            List<StepExecutionDTO> stepExecutionDTOs = jobExecution.getStepExecutions()
                    .stream()
                    .map(stepExecution -> new StepExecutionDTO(
                            stepExecution.getId(),
                            stepExecution.getStepName(),
                            stepExecution.getStatus().toString(),
                            stepExecution.getExitStatus().getExitCode(),
                            stepExecution.getReadCount(),
                            stepExecution.getFilterCount(),
                            stepExecution.getWriteCount(),
                            stepExecution.getReadSkipCount(),
                            stepExecution.getWriteSkipCount(),
                            stepExecution.getProcessSkipCount(),
                            stepExecution.getCommitCount(),
                            stepExecution.getRollbackCount()
                    ))
                    .collect(Collectors.toList());

            jobExecutionDTO.setStepExecutions(stepExecutionDTOs);

            return ResponseEntity.status(HttpStatus.CREATED).body(jobExecutionDTO);
        } catch (JobExecutionAlreadyRunningException | JobRestartException |
                 JobInstanceAlreadyCompleteException | JobParametersInvalidException |
                 InterruptedException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null);
        }
    }

}


