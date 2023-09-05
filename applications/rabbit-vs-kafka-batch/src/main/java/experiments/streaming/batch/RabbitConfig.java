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
import experiments.streaming.domain.Transaction;

import java.util.List;

@Configuration
@Profile("rabbit")
@Slf4j
public class RabbitConfig {

    @Value("${spring.rabbitmq.stream.name}")
    private String streamName;


    @Value("${spring.batch.size:10000}")
    private int batchSize;

    @Value("${spring.rabbitmq.stream.uri}")
    private List<String> uris;

    @Value("${spring.rabbitmq.stream.username}")
    private String username;

    @Value("${spring.rabbitmq.stream.password}")
    private String password;

    @Bean
    Environment rabbitEnv()
    {
        var env = Environment.builder()
                .uris(uris).username(username)
                .password(password).build();
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
    RabbitMQStreamWriter itemWriter(Producer producer, Converter<Transaction, byte[]> converter)
    {
        return new RabbitMQStreamWriter(producer,converter);
    }
}
