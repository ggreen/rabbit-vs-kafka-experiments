package experiments.streaming.batch;

import experiments.streaming.batch.kafka.KafkaProducerItemWriter;
import lombok.extern.slf4j.Slf4j;
import nyla.solutions.core.patterns.conversion.Converter;
import nyla.solutions.core.util.Config;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.kafka.config.TopicBuilder;
import experiments.streaming.domain.Payment;
import java.util.Map;

/**
 * @author gregory green
 */
@Configuration
@Profile("kafka")
@Slf4j
public class KafkaConfig {

    @Value("${batch.apache.kafka.topic.name:transactions}")
    private String topicName;


    @Bean
    Producer<String,byte[]> kafkaProducer()
    {
        return new KafkaProducer<String,byte[]>((Map)Config.getProperties());
    }

    @Bean
    ItemWriter<Payment> writer(Producer<String,byte[]> producer, Converter<Payment,byte[]> converter)
    {
        var writer = new KafkaProducerItemWriter(producer,converter,topicName);
        return writer;
    }

    @Bean
    NewTopic topicBuilder()
    {
        return TopicBuilder.name(topicName).build();
    }

}
