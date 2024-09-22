package com.kelsonthony.demofoliointegrationmodeusers.infrastructure.config;

import com.kelsonthony.demofoliointegrationmodeusers.app.dto.UserDTO;
import com.kelsonthony.demofoliointegrationmodeusers.batch.writer.UserItemWriter;
import com.kelsonthony.demofoliointegrationmodeusers.infrastructure.partition.ColumnRangePartitioner;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.SkipListener;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.partition.PartitionHandler;
import org.springframework.batch.core.partition.support.TaskExecutorPartitionHandler;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.skip.SkipPolicy;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@EnableBatchProcessing
public class BatchJobConfig {

    private final JobRepository jobRepository;
    private final PlatformTransactionManager transactionManager;
    private final UserItemWriter userItemWriter;
    private final TaskExecutor taskExecutor;
    private final SkipPolicy skipPolicy;
    private final SkipListener<UserDTO, Number> skipListener;
    private final JobExecutionListener jobCompletionListener;
    private final ItemProcessor<UserDTO, UserDTO> processor;
    private final ItemReader<UserDTO> reader;

    public BatchJobConfig(JobRepository jobRepository,
                          PlatformTransactionManager transactionManager,
                          UserItemWriter userItemWriter,
                          TaskExecutor taskExecutor,
                          SkipPolicy skipPolicy,
                          SkipListener<UserDTO, Number> skipListener,
                          JobExecutionListener jobCompletionListener,
                          ItemProcessor<UserDTO, UserDTO> processor,
                          ItemReader<UserDTO> reader) {
        this.jobRepository = jobRepository;
        this.transactionManager = transactionManager;
        this.userItemWriter = userItemWriter;
        this.taskExecutor = taskExecutor;
        this.skipPolicy = skipPolicy;
        this.skipListener = skipListener;
        this.jobCompletionListener = jobCompletionListener;
        this.processor = processor;
        this.reader = reader;
    }

    @Bean
    @StepScope
    public ColumnRangePartitioner partitioner(@Value("#{stepExecutionContext['totalRows']}") Integer totalRows) {
        if (totalRows == null) {
            totalRows = 1000000;
        }
        return new ColumnRangePartitioner(totalRows);
    }

    @Bean
    public PartitionHandler partitionHandler() {
        TaskExecutorPartitionHandler partitionHandler = new TaskExecutorPartitionHandler();
        partitionHandler.setTaskExecutor(taskExecutor);
        partitionHandler.setStep(slaveStep());
        partitionHandler.setGridSize(2);
        return partitionHandler;
    }

    @Bean
    public Step masterStep() {
        return new StepBuilder("masterStep", jobRepository)
                .partitioner(slaveStep().getName(), partitioner(null))
                .partitionHandler(partitionHandler())
                .build();
    }

    @Bean
    public Step slaveStep() {
        return new StepBuilder("slaveStep", jobRepository)
                .<UserDTO, UserDTO>chunk(1000, transactionManager)
                .reader(reader)
                .processor(processor)
                .writer(userItemWriter)
                .faultTolerant()
                .skipPolicy(skipPolicy)
                .listener(skipListener)
                .build();
    }

    @Bean
    public Job runJob() {
        return new JobBuilder("importUsers", jobRepository)
                .incrementer(new RunIdIncrementer())
                .listener(jobCompletionListener)
                .flow(masterStep())
                .end()
                .build();
    }
}