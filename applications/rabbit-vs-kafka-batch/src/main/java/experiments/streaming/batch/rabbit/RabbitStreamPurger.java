package experiments.streaming.batch.rabbit;

import com.rabbitmq.stream.Environment;
import com.rabbitmq.stream.EnvironmentBuilder;
import lombok.RequiredArgsConstructor;
import nyla.solutions.core.util.Config;
import nyla.solutions.core.util.settings.Settings;

import java.util.Arrays;

import static java.util.Arrays.asList;

/**
 * Delete/recreate a RabbitMQ stream
 * @author gregory green
 */
@RequiredArgsConstructor
public class RabbitStreamPurger {
    private final EnvironmentBuilder builder;

    public RabbitStreamPurger(Settings settings,EnvironmentBuilder builder) {

        this.builder = builder.uris(
                asList(settings
                        .getPropertyStrings("spring.rabbitmq.stream.uris")))
                .username(settings.getProperty("spring.rabbitmq.stream.username"))
                .password(settings.getProperty("spring.rabbitmq.stream.password"));
    }

    public static void purgeStream(String[] args) {
        var settings = Config.loadArgs(args);

        var purger = new RabbitStreamPurger(settings, Environment.builder());
        var streamName = settings.getProperty("rabbitmq.stream.name");

        purger.purge(streamName);
    }

    public void purge(String streamName) {

        try(var environment = builder.build())
        {
            try{environment.deleteStream(streamName);} catch (Exception e) {}

            environment.streamCreator().stream(streamName).create();
        }
    }
}
