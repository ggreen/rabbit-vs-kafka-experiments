package experiments.streaming.batch.rabbit.writer;

import com.rabbitmq.stream.ConfirmationHandler;
import com.rabbitmq.stream.Producer;
import nyla.solutions.core.patterns.conversion.Converter;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import experiments.streaming.domain.Transaction;

import java.util.concurrent.atomic.AtomicLong;

/**
 * @author gregory green
 */
public class RabbitMQStreamWriter implements ItemWriter<Transaction> {

    private final Producer producer;
    private final Converter<Transaction,byte[]> serializer;
    private final ConfirmationHandler handler;
    private static final AtomicLong count = new AtomicLong();


    public RabbitMQStreamWriter(Producer producer,
                                Converter<Transaction,byte[]> serializer ) {
        this.producer = producer;
        this.serializer = serializer;
        //Publish Confirm with an atomic long
        this.handler = confirmationStatus -> {count.addAndGet(1);};
    }

    @Override
    public void write(Chunk<? extends Transaction> items) throws Exception {
        items.forEach(transaction ->
                producer.send(producer.messageBuilder()
                        .addData(serializer.convert(transaction)).build(),handler));
    }

    public long count() {
        return count.get();
    }
}

