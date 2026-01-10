package experiments.streaming.batch;

import experiments.streaming.domain.Transaction;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.job.Job;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.job.parameters.JobParameter;
import org.springframework.batch.core.job.parameters.JobParameters;
import org.springframework.batch.core.job.parameters.JobParametersBuilder;
import org.springframework.batch.core.job.parameters.RunIdIncrementer;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.JobOperator;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.Step;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.infrastructure.item.ItemWriter;
import org.springframework.batch.infrastructure.item.file.FlatFileItemReader;
import org.springframework.batch.infrastructure.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.infrastructure.item.file.mapping.RecordFieldSetMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.transaction.PlatformTransactionManager;

import java.time.LocalDateTime;


@Configuration
@EnableBatchProcessing
//        (tablePrefix = "${spring.liquibase.defaultSchema}.batch_")
@Slf4j
public class BatchConfig {

    @Value("${batch.file.location}")
    private String fileLocation;

    @Value("${batch.chunk.size}")
    private int chunkSize;

    @Value("${batch.core.pool.size}")
    private int corePoolSize;

    @Value("${spring.application.name}")
    private String applicationName;

    @Value("${spring.profiles.active}")
    private String jobName;

    @Value("${spring.profiles.active}-process")
    private String stepName;

    @Bean
    FlatFileItemReader reader()
    {
       return  new FlatFileItemReaderBuilder<Transaction>()
               .name(applicationName+"-reader")
               .linesToSkip(1) //skip header
               .delimited()
               .names(new String[]{"id","details","contact","location","amount","timestamp"})
               .fieldSetMapper(new RecordFieldSetMapper<Transaction>(Transaction.class))
               .resource(new FileSystemResource(fileLocation))
               .build();
    }

    @Bean
    public Job importUserJob(
            JobRepository jobRepository, Step step) {
        return new JobBuilder(jobName,jobRepository)
                .start(step)
                .incrementer(new RunIdIncrementer())
                .build();
    }

    @Bean
    CommandLineRunner runner(JobOperator jobOperator, Job job)
    {
        return args -> {

            JobParameter<?> jobParameter = new JobParameter<LocalDateTime>("time",
                    LocalDateTime.now(),LocalDateTime.class);

            JobParameters jobParameters = new JobParametersBuilder()
                    .addJobParameter(jobParameter).toJobParameters();
            jobOperator.start(job, jobParameters);

        };
    }

    @Bean
    public Step step(ItemWriter<Transaction> writer,
                     JobRepository jobRepository,
                     PlatformTransactionManager transactionManager,
                     ThreadPoolTaskExecutor taskExecutor) {

        taskExecutor.setCorePoolSize(corePoolSize);

        return new StepBuilder(jobRepository)
                .<String, String>chunk(10).transactionManager(transactionManager)
                .reader(reader())
                .writer(writer)
                .build();
    }


}
