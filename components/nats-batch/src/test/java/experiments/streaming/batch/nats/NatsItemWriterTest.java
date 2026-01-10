package experiments.streaming.batch.nats;

import experiments.streaming.domain.Transaction;
import io.nats.client.JetStream;
import io.nats.client.api.PublishAck;
import lombok.SneakyThrows;
import nyla.solutions.core.patterns.conversion.Converter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.batch.infrastructure.item.Chunk;

import java.nio.charset.StandardCharsets;

import static java.util.Arrays.asList;
import static nyla.solutions.core.patterns.creational.generator.JavaBeanGeneratorCreator.of;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class NatsItemWriterTest {

    private NatsItemWriter subject;

    private Chunk<Transaction> list = new Chunk<>(asList(of(Transaction.class).create()));

    @Mock
    private JetStream js;
    @Mock
    private Converter<Transaction,byte[]> converter;
    @Mock
    private PublishAck ack;
    private String subscribeSubject = "test";

    @BeforeEach
    void setUp() {
        subject = new NatsItemWriter(js,converter,subscribeSubject);
    }

    @SneakyThrows
    @Test
    void write() {
        when(converter.convert(any())).thenReturn("hello".getBytes(StandardCharsets.UTF_8));
        when(js.publish(anyString(),any( byte[].class))).thenReturn(ack);

        subject.write(list);
        verify(js).publish(anyString(),any(byte[].class));
    }
}