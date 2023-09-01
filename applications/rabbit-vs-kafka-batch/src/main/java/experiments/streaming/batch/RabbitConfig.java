package experiments.streaming.batch;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.stream.Environment;
import com.rabbitmq.stream.Producer;
import experiments.streaming.batch.rabbit.writer.RabbitMQStreamWriter;
import lombok.extern.slf4j.Slf4j;
import nyla.solutions.core.patterns.conversion.Converter;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import showcase.high.throughput.microservices.domain.Payment;

import java.nio.charset.StandardCharsets;

@Configuration
@Profile("rabbit")
@Slf4j
public class RabbitConfig {

    public RabbitConfig()
    {
        log.info("Creating");
    }

    @Value("${rabbitmq.stream.name}")
    private String streamName;


    @Value("${rabbitmq.stream.producer.batch.size:500}")
    private int batchSize;

    @Bean
    Environment rabbitEnv()
    {
        var env = Environment.builder().build();
        return env;
    }

    @Bean
    ObjectMapper objectMapper()
    {
        return new ObjectMapper();
    }

    @Bean
    Converter<Payment,byte[]> serializer(ObjectMapper objectMapper)
    {
        return transaction -> {
            try {
                return objectMapper.writeValueAsString(transaction).getBytes(StandardCharsets.UTF_8);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        };
    }

    @Bean
    Producer producer(Environment environment)
    {
        return environment.producerBuilder().stream(streamName)
                .batchSize(batchSize)
                .build();
    }

    @Bean
    JobExecutionListener listener(Environment environment, RabbitMQStreamWriter writer)
    {
        return new JobExecutionListener() {

            @Override
            public void beforeJob(JobExecution jobExecution) {

//                log.info("Deleting stream {} ",streamName);
//
//                environment.deleteStream(streamName);
//                log.info("Deleted stream {} ",streamName);
//
//                environment.streamCreator().stream(streamName).create();
            }


            @Override
            public void afterJob(JobExecution jobExecution) {
                log.info("****** Process COUNT: {}",writer.count());
            }
        };
    }

    @Bean
    RabbitMQStreamWriter itemWriter(Producer producer, Converter<Payment, byte[]> converter)
    {
        return new RabbitMQStreamWriter(producer,converter);
    }
}
