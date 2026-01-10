package experiments.streaming.batch;

import nyla.solutions.core.patterns.conversion.Converter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import experiments.streaming.domain.Transaction;
import tools.jackson.databind.json.JsonMapper;

import java.nio.charset.StandardCharsets;

@Configuration
public class SerializationConfig {

    @Bean
    JsonMapper objectMapper()
    {
        return new JsonMapper ();
    }

    @Bean
    Converter<Transaction,byte[]> serializer(JsonMapper  objectMapper)
    {
        return transaction -> {
                return objectMapper.writeValueAsString(transaction).getBytes(StandardCharsets.UTF_8);
        };
    }
}
