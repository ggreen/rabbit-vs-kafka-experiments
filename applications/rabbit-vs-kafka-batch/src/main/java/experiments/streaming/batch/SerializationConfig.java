package experiments.streaming.batch;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import nyla.solutions.core.patterns.conversion.Converter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import showcase.high.throughput.microservices.domain.Payment;

import java.nio.charset.StandardCharsets;

@Configuration
public class SerializationConfig {
    @Bean
    ObjectMapper objectMapper()
    {
        return new ObjectMapper();
    }

    @Bean
    Converter<Payment,byte[]> serializer(ObjectMapper objectMapper)
    {
        return transaction -> {
            try {
                return objectMapper.writeValueAsString(transaction).getBytes(StandardCharsets.UTF_8);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        };
    }
}
