//package com.kelsonthony.demofoliointegrationmodeusers.infrastructure.listener;
//
//import com.kelsonthony.demofoliointegrationmodeusers.infrastructure.util.ExcelRowCounter;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Component;
//import org.springframework.batch.core.JobExecution;
//import org.springframework.batch.core.JobExecutionListener;
//
//@Component
//public class JobCompletionNotificationListener implements JobExecutionListener {
//
//    @Autowired
//    private ExcelRowCounter excelRowCounter;
//
//    @Value("${report.input.filepath}")
//    private String inputFilepath;
//
//    @Override
//    public void beforeJob(JobExecution jobExecution) {
//        int totalRows = excelRowCounter.countRows(inputFilepath);
//        jobExecution.getExecutionContext().putInt("totalRows", totalRows);
//    }
//
//    @Override
//    public void afterJob(JobExecution jobExecution) {
//        // Código após a execução do job
//    }
//}