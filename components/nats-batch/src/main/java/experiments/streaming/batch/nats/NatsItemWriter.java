package experiments.streaming.batch.nats;

import experiments.streaming.domain.Transaction;
import io.nats.client.JetStream;
import io.nats.client.JetStreamApiException;
import nyla.solutions.core.patterns.conversion.Converter;
import org.springframework.batch.infrastructure.item.Chunk;
import org.springframework.batch.infrastructure.item.ItemWriter;

import java.io.IOException;


public class NatsItemWriter implements ItemWriter<Transaction> {

    private final JetStream jetStream;
    private final String subscribeSubject;
    private final  Converter<Transaction, byte[]> serializer;

    public NatsItemWriter(JetStream jetStream, Converter<Transaction, byte[]> serializer,
                          String subscribeSubject) {
        this.subscribeSubject = subscribeSubject;
        this.serializer = serializer;
        this.jetStream = jetStream;
    }

    @Override
    public void write(Chunk<? extends Transaction> items) throws Exception {
        items.forEach(transaction ->
        {
            try {
                var ack = jetStream.publish(subscribeSubject,
                        serializer.convert(transaction)
                );

                ack.throwOnHasError();
            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (JetStreamApiException e) {
                throw new RuntimeException(e);
            }
        });
    }
}
