package experiments.streaming.batch.rabbit;

import com.rabbitmq.stream.Message;
import com.rabbitmq.stream.MessageBuilder;
import com.rabbitmq.stream.Producer;
import experiments.streaming.batch.rabbit.writer.RabbitMQStreamWriter;
import nyla.solutions.core.patterns.conversion.Converter;
import nyla.solutions.core.patterns.creational.generator.JavaBeanGeneratorCreator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.batch.item.Chunk;
import experiments.streaming.domain.Payment;

import static java.util.Arrays.asList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RabbitMQStreamWriterTest {

    @Mock
    private Producer producer;
    private RabbitMQStreamWriter subject;

    @Mock
    private MessageBuilder messageBuilder;
    private Payment expected = JavaBeanGeneratorCreator.of(Payment.class).create();
    @Mock
    private Converter<Payment,byte[]> serializer;

    @Mock
    private Message message;

    @Mock
    private MessageBuilder.ApplicationPropertiesBuilder propertiesBuilders;
    private long expectedCount = 0;
    private Chunk<Payment> list;


    @BeforeEach
    void setUp() {
        subject = new RabbitMQStreamWriter(producer, serializer);
        list = new Chunk<>(asList(expected));
    }

    @Test
    void given_transaction_when_write_then_publish() throws Exception {

        when(producer.messageBuilder()).thenReturn( messageBuilder);
        when(messageBuilder.applicationProperties()).thenReturn(propertiesBuilders);
        when(propertiesBuilders.entry(anyString(),anyString())).thenReturn(propertiesBuilders);
        when(propertiesBuilders.messageBuilder()).thenReturn(messageBuilder);
        when(messageBuilder.addData(any())).thenReturn(messageBuilder);
        when(messageBuilder.build()).thenReturn(message);

        subject.write(list);

        verify(producer).send(any(),any());
    }

    @DisplayName("GIVEN send WHEN count THE count is expected")
    @Test
    void count() throws Exception {
        assertEquals(expectedCount, subject.count());
    }
}