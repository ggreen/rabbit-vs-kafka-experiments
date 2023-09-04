package experiments.streaming.report;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewResolverRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void configureViewResolvers(ViewResolverRegistry registry) {
        registry.freeMarker();
    }


    @Bean
    public FreeMarkerConfigurer freeMarkerConfigurer() {
        FreeMarkerConfigurer configurer = new FreeMarkerConfigurer();
        configurer.setTemplateLoaderPath("classpath:WEB-INF/freemarker");
        return configurer;
    }

//    @Bean
//    FreeMarkerViewResolver viewResolver()
//    {
//        FreeMarkerViewResolver i= new FreeMarkerViewResolver();
//        i.setCache(true);
//        i.setPrefix("");
//        i.setSuffix(".ftl");
//        return i;
//    }//------------------------------------------------
//
//    @SneakyThrows
//    @Bean
//    public FreeMarkerConfigurer freemarkerConfig()
//    {
//        FreeMarkerConfigurationFactory factory = new FreeMarkerConfigurationFactory();
//        factory.setTemplateLoaderPath("classpath:templates");
//        factory.setDefaultEncoding("UTF-8");
//        FreeMarkerConfigurer result = new FreeMarkerConfigurer();
//        result.setConfiguration(factory.createConfiguration());
//        return result;
//    }
}
