package experiments.streaming.report;

import lombok.SneakyThrows;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.ui.freemarker.FreeMarkerConfigurationFactory;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;
import org.springframework.web.servlet.view.freemarker.FreeMarkerViewResolver;

@Configuration
public class WebConfig {
    @Bean
    FreeMarkerViewResolver viewResolver()
    {
        FreeMarkerViewResolver i= new FreeMarkerViewResolver();
        i.setCache(true);
        i.setPrefix("");
        i.setSuffix(".ftl");
        return i;
    }//------------------------------------------------

    @SneakyThrows
    @Bean
    public FreeMarkerConfigurer freemarkerConfig()
    {
        FreeMarkerConfigurationFactory factory = new FreeMarkerConfigurationFactory();
        factory.setTemplateLoaderPath("classpath:templates");
        factory.setDefaultEncoding("UTF-8");
        FreeMarkerConfigurer result = new FreeMarkerConfigurer();
        result.setConfiguration(factory.createConfiguration());
        return result;
    }
}
