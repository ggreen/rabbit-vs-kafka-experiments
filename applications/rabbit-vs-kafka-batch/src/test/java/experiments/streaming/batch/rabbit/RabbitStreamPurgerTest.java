package experiments.streaming.batch.rabbit;

import com.rabbitmq.stream.Environment;
import com.rabbitmq.stream.EnvironmentBuilder;
import com.rabbitmq.stream.StreamCreator;
import nyla.solutions.core.util.settings.Settings;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RabbitStreamPurgerTest {
    private String streamName = "test";

    @Mock
    private EnvironmentBuilder builder;

    @Mock
    private Environment environment;

    @Mock
    private StreamCreator streamCreator;

    @Mock
    private Settings settings;

    private RabbitStreamPurger subject;
    private String password = "password";
    private String userName  = "user";

    @BeforeEach
    void setUp() {
        subject = new RabbitStreamPurger(builder);
    }

    @Test
    void constructFromSetting() {
        String[] uris  = {"rabbitmq-stream://host1:5552",
                "rabbitmq-stream://host2:5552"};



        when(settings.getPropertyStrings(anyString())).thenReturn(uris);
        when(settings.getProperty(anyString())).thenReturn(userName)
                .thenReturn(password);

        when(builder.uris(any(List.class))).thenReturn(builder);
        when(builder.username(anyString())).thenReturn(builder);
        when(builder.password(anyString())).thenReturn(builder);

        var subject = new RabbitStreamPurger(settings,builder);

        verify(builder).uris(any());
        verify(builder).username(anyString());
        verify(builder).password(anyString());
    }

    @Test
    void purge() {

        when(builder.build()).thenReturn(environment);
        when(environment.streamCreator()).thenReturn(streamCreator);
        when(streamCreator.stream(anyString())).thenReturn(streamCreator);

        subject.purge(streamName);

        verify(environment).deleteStream(anyString());
        verify(streamCreator).create();

    }
}