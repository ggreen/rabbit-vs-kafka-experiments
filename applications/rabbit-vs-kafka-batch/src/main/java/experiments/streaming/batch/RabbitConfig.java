package experiments.streaming.batch;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.stream.Environment;
import com.rabbitmq.stream.Producer;
import experiments.streaming.batch.writer.RabbitMQStreamWriter;
import nyla.solutions.core.patterns.conversion.Converter;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import showcase.high.throughput.microservices.domain.Payment;

import java.nio.charset.StandardCharsets;

@Configuration
@Profile("rabbitmq")
public class RabbitConfig {

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
    ItemWriter<Payment> itemWriter(Producer producer, Converter<Payment, byte[]> converter)
    {
        return new RabbitMQStreamWriter(producer,converter);
    }
}
