package experiments.streaming.batch;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.item.kafka.KafkaItemWriter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.converter.JsonMessageConverter;
import showcase.high.throughput.microservices.domain.Payment;

@Configuration
@Profile("kafka")
@Slf4j
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

    @Bean
    NewTopic topicBuilder()
    {
        return TopicBuilder.name(topicName).build();
    }

    @Bean
    JobExecutionListener listener()
    {
        return new JobExecutionListener() {

            @Override
            public void afterJob(JobExecution jobExecution) {
                log.info("****** Process Complete ");
            }
        };
    }
}
