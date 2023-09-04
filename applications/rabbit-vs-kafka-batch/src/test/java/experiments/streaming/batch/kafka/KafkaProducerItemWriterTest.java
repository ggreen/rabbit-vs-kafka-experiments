package experiments.streaming.batch.kafka;

import nyla.solutions.core.patterns.conversion.Converter;
import nyla.solutions.core.patterns.creational.generator.JavaBeanGeneratorCreator;
import org.apache.kafka.clients.producer.Producer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.batch.item.Chunk;
import experiments.streaming.domain.Payment;

import static java.util.Arrays.asList;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class KafkaProducerItemWriterTest {

    private KafkaProducerItemWriter subject;
    @Mock
    private Producer<String,byte[]> producer;
    @Mock
    private Converter<Payment,byte[]> converter;

    private Chunk<Payment> list = new Chunk<>(asList(JavaBeanGeneratorCreator.of(Payment.class).create()));
    private String topicName ="topic";

    @BeforeEach
    void setUp() {
        subject =  new KafkaProducerItemWriter(producer,converter,topicName);
    }

    @Test
    void write() throws Exception {

        subject.write(list);
        verify(producer).send(any());
    }
}