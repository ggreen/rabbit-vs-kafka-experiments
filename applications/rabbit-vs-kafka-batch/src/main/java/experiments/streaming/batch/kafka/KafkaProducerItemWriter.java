package experiments.streaming.batch.kafka;

import nyla.solutions.core.patterns.conversion.Converter;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import experiments.streaming.domain.Payment;

public class KafkaProducerItemWriter implements ItemWriter<Payment> {

    private final Producer<String, byte[]> producer;
    private final Converter<Payment,byte[]> serializer;
    private final String topicName;

    public KafkaProducerItemWriter(Producer<String, byte[]> producer,
                                   Converter<Payment, byte[]> serializer,
                                   String topicName) {
        this.producer = producer;
        this.serializer = serializer;
        this.topicName = topicName;
    }

    @Override
    public void write(Chunk<? extends Payment> items) throws Exception {
            items.forEach(transaction ->
                    producer.send(new ProducerRecord<String,byte[]>(
                            topicName,
                            transaction.id(),
                            serializer.convert(transaction)
                    )));
    }
}
