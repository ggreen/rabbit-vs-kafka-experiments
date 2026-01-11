package experiments.streaming.batch.kafka;

import nyla.solutions.core.patterns.conversion.Converter;
import nyla.solutions.core.patterns.creational.generator.JavaBeanGeneratorCreator;
import org.apache.kafka.clients.producer.Producer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import experiments.streaming.domain.Transaction;
import org.springframework.batch.infrastructure.item.Chunk;

import static java.util.Arrays.asList;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class KafkaProducerItemWriterTest {

    private KafkaProducerItemWriter subject;
    @Mock
    private Producer<String,byte[]> producer;
    @Mock
    private Converter<Transaction,byte[]> converter;

    private final Chunk<Transaction> list = new Chunk<>(asList(JavaBeanGeneratorCreator.of(Transaction.class).create()));
    private final String topicName ="topic";

    @BeforeEach
    void setUp() {
        subject =  new KafkaProducerItemWriter(producer,converter,topicName);
    }

    @Test
    void write() throws Exception {

        subject.write(list);
        verify(producer).send(any(),any());
    }
}