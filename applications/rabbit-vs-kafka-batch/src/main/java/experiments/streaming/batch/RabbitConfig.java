package experiments.streaming.batch;

import com.rabbitmq.stream.Environment;
import com.rabbitmq.stream.Producer;
import experiments.streaming.batch.rabbit.writer.RabbitMQStreamWriter;
import lombok.extern.slf4j.Slf4j;
import nyla.solutions.core.patterns.conversion.Converter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import showcase.high.throughput.microservices.domain.Payment;

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
        env.streamCreator().stream(streamName).create();
        return env;
    }



    @Bean
    Producer producer(Environment environment)
    {
        return environment.producerBuilder().stream(streamName)
                .batchSize(batchSize)
                .build();
    }

    @Bean
    RabbitMQStreamWriter itemWriter(Producer producer, Converter<Payment, byte[]> converter)
    {
        return new RabbitMQStreamWriter(producer,converter);
    }
}
