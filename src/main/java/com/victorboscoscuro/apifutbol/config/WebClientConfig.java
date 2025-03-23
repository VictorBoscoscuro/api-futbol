package com.victorboscoscuro.apifutbol.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    @Bean
    public WebClient webClient(@Value("${api.token}") String token) {
        return WebClient.builder()
                .baseUrl("https://api.football-data.org/v4")
                .defaultHeader("X-Auth-Token", token)
                .defaultHeader("Accept", "application/json")
                .defaultHeader("Content-Type", "application/json")
                .build();
    }
}