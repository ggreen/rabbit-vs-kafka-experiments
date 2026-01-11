package experiments.streaming.batch.kafka;

import nyla.solutions.core.patterns.conversion.Converter;
import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import experiments.streaming.domain.Transaction;
import org.springframework.batch.infrastructure.item.Chunk;
import org.springframework.batch.infrastructure.item.ItemWriter;

import java.util.concurrent.atomic.AtomicLong;

public class KafkaProducerItemWriter implements ItemWriter<Transaction> {

    private final Producer<String, byte[]> producer;
    private final Converter<Transaction,byte[]> serializer;
    private final String topicName;
    private static final AtomicLong count = new AtomicLong();
    private final Callback handler;

    public KafkaProducerItemWriter(Producer<String, byte[]> producer,
                                   Converter<Transaction, byte[]> serializer,
                                   String topicName) {
        this.producer = producer;
        this.serializer = serializer;
        this.topicName = topicName;
        this.handler = (recordMetadata, e) -> {
            count.addAndGet(1);
        };
    }

    @Override
    public void write(Chunk<? extends Transaction> items) throws Exception {
            items.forEach(transaction ->
                    producer.send(new ProducerRecord<String,byte[]>(
                            topicName,
                            transaction.id(),
                            serializer.convert(transaction)
                    ), handler));
    }
}
