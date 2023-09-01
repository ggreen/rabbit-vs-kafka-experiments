package experiments.streaming.batch;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.*;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.launch.support.TaskExecutorJobLauncher;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.mapping.RecordFieldSetMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.transaction.PlatformTransactionManager;
import showcase.high.throughput.microservices.domain.Payment;

import java.time.LocalDateTime;

import static nyla.solutions.core.util.Organizer.toMap;

@Configuration
@EnableBatchProcessing(tablePrefix = "${spring.liquibase.defaultSchema}.batch_")
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
       return  new FlatFileItemReaderBuilder<Payment>()
               .name(applicationName+"-reader")
               .linesToSkip(1) //skip header
               .delimited()
               .names(new String[]{"id","details","contact","location","amount","timestamp"})
               .fieldSetMapper(new RecordFieldSetMapper<Payment>(Payment.class))
               .resource(new FileSystemResource(fileLocation))
               .build();
    }

    @Bean
    public JobLauncher jobLauncher(JobRepository jobRepository) throws Exception {
        TaskExecutorJobLauncher jobLauncher = new TaskExecutorJobLauncher();
        jobLauncher.setJobRepository(jobRepository);
        jobLauncher.afterPropertiesSet();
        return jobLauncher;
    }

    @Bean
    public Job importUserJob(
            JobRepository jobRepository,
                             JobExecutionListener listener, Step step) {
        return new JobBuilder(jobName,jobRepository)
                .start(step)
                .incrementer(new RunIdIncrementer())
                .listener(listener)
                .build();
    }

    @Bean
    CommandLineRunner runner(JobLauncher jobLauncher, Job job)
    {
        return args -> {

            jobLauncher.run(job,new JobParameters(
                    toMap("start",
                            new JobParameter<LocalDateTime>(LocalDateTime.now(),
                                    LocalDateTime.class))));

        };
    }

    @Bean
    public Step step(ItemWriter<Payment> writer,
                     JobRepository jobRepository,
                     PlatformTransactionManager transactionManager,
                     ThreadPoolTaskExecutor taskExecutor) {

        taskExecutor.setCorePoolSize(corePoolSize);

        return new StepBuilder(stepName,jobRepository)
                .<Payment, Payment> chunk(chunkSize, transactionManager)
                .reader(reader())
                .writer(writer)
                .taskExecutor(taskExecutor)
                .build();
    }

}
