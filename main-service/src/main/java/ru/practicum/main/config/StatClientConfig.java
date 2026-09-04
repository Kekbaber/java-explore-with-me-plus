package ru.practicum.main.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.practicum.stat.client.StatClient;

@Configuration
public class StatClientConfig {

    @Bean
    public StatClient statClient(@Value("${stat.server.url:http://localhost:9090}") String baseUrl) {
        return new StatClient(baseUrl);
    }
}
