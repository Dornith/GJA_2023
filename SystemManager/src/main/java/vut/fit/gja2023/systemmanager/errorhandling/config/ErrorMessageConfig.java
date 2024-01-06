package vut.fit.gja2023.systemmanager.errorhandling.config;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;

import java.nio.charset.StandardCharsets;
import java.util.Locale;

@Configuration
public class ErrorMessageConfig {

    @Bean
    public MessageSource errorMessageSource() {
        ResourceBundleMessageSource outcome = new ResourceBundleMessageSource();
        outcome.setBasenames("errorMessages");
        outcome.setDefaultLocale(Locale.ENGLISH);
        outcome.setDefaultEncoding(StandardCharsets.UTF_8.name());
        return outcome;
    }

}