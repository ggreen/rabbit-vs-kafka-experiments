package experiments.streaming.batch;

import org.springframework.batch.item.kafka.KafkaItemWriter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.converter.JsonMessageConverter;
import showcase.high.throughput.microservices.domain.Payment;

@Configuration
@Profile("kafka")
public class KafkaConfig {

    @Value("${spring.kafka.producer.value-serializer}")
    private String producerValueSerializer;

    @Value("${spring.kafka.producer.acks}")
    private String producerAcks;

    @Value("${batch.apache.kafka.topic.name:test-transactions}")
    private String topicName;

    @Bean
    KafkaItemWriter<String, Payment> writer(KafkaTemplate<String, Payment> template)
    {
        template.setMessageConverter(new JsonMessageConverter());
        template.setDefaultTopic(topicName);

        var writer = new KafkaItemWriter<String, Payment>();
        writer.setKafkaTemplate(template);
        writer.setItemKeyMapper(transaction -> transaction.id() );
        return writer;
    }
}
