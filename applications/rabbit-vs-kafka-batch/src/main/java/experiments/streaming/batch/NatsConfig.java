package experiments.streaming.batch;

import experiments.streaming.batch.nats.NatsItemWriter;
import experiments.streaming.domain.Transaction;
import io.nats.client.*;
import io.nats.client.api.StreamConfiguration;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import nyla.solutions.core.patterns.conversion.Converter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.time.Duration;

@Configuration
@Profile("nats")
@Slf4j
public class NatsConfig {

    @Value("${nats.server.url}")
    private String natsServerURL;

    @Value("${spring.application.name}")
    private String applicationName;

    @Value("${nats.subscribe.subject}")
    private String subscribeSubject;

    @SneakyThrows
    @Bean
    Connection natsConnection()
    {
        Options options = new Options.Builder().
                server(natsServerURL).
                connectionName(applicationName). // Set Name
                        reconnectWait(Duration.ofSeconds(10)).
                reconnectBufferSize(5 * 1024 * 1024).  // Set buffer in bytes
                        build();

        return Nats.connect(options);
    }

    @SneakyThrows
    @Bean
    JetStreamManagement constructStream(Connection connection)
    {
        var jsm = connection.jetStreamManagement();
        StreamConfiguration config = new StreamConfiguration
                .Builder()
                .name(subscribeSubject)
                .build();

        jsm.addStream(config);

        return jsm;
    }


    @SneakyThrows
    @Bean
    JetStream jetStream(Connection connection)
    {
        return connection.jetStream();
    }

    @Bean
    NatsItemWriter itemWriter(JetStream jetStream, Converter<Transaction, byte[]> converter)
    {
        return new NatsItemWriter(jetStream,converter,subscribeSubject);
    }
}
