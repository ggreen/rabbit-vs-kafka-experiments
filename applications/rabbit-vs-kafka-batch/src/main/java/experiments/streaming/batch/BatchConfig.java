package experiments.streaming.batch;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.support.TaskExecutorJobLauncher;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.mapping.RecordFieldSetMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.transaction.PlatformTransactionManager;
import showcase.high.throughput.microservices.domain.Payment;

@Configuration
@EnableBatchProcessing
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
    public Job importUserJob(
            JobRepository jobRepository,
                             JobExecutionListener listener, Step step1) {
        return new JobBuilder(applicationName,jobRepository)
                .start(step1)
                .listener(listener)
                .build();
    }

    @Bean
    JobExecutionListener listener(JdbcTemplate jdbcTemplate)
    {
        return new JobExecutionListener() {
            @Override
            public void beforeJob(JobExecution jobExecution) {
                log.info("START");
            }

            @Override
            public void afterJob(JobExecution jobExecution) {
                log.info("END");
            }
        };
    }
    @Bean
    public Step step1(ItemWriter<Payment> writer,
                      JobRepository jobRepository,
                      PlatformTransactionManager transactionManager,
                      ThreadPoolTaskExecutor taskExecutor) {

        taskExecutor.setCorePoolSize(corePoolSize);

        return new StepBuilder("step1",jobRepository)
                .<Payment, Payment> chunk(chunkSize, transactionManager)
                .reader(reader())
                .writer(writer)
                .taskExecutor(taskExecutor)
                .build();
    }

}
