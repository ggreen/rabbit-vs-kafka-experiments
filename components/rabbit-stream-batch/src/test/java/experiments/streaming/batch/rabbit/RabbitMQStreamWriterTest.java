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
import experiments.streaming.domain.Transaction;
import org.springframework.batch.infrastructure.item.Chunk;

import static java.util.Arrays.asList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Test for RabbitMQStreamWriter
 * @author gregory green
 */
@ExtendWith(MockitoExtension.class)
class RabbitMQStreamWriterTest {

    @Mock
    private Producer producer;
    private RabbitMQStreamWriter subject;

    @Mock
    private MessageBuilder messageBuilder;
    private Transaction expected = JavaBeanGeneratorCreator.of(Transaction.class).create();
    @Mock
    private Converter<Transaction,byte[]> serializer;

    @Mock
    private Message message;

    @Mock
    private MessageBuilder.ApplicationPropertiesBuilder propertiesBuilders;
    private long expectedCount = 0;
    private Chunk<Transaction> list;


    @BeforeEach
    void setUp() {
        subject = new RabbitMQStreamWriter(producer, serializer);
        list = new Chunk<>(asList(expected));
    }

    @DisplayName("GIVEN transaction WHEN write THEN publish")
    @Test
    void publish() throws Exception {

        when(producer.messageBuilder()).thenReturn( messageBuilder);
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