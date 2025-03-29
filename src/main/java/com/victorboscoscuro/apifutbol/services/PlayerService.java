package com.victorboscoscuro.apifutbol.services;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class PlayerService {

    private final WebClient webClient;

    public PlayerService(WebClient webClient) {
        this.webClient = webClient;
    }

    public Mono<String> getPlayer(Integer playerId) {
        return webClient.get()
                .uri("/persons/{playerId}", playerId)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, response -> {
                    if (response.statusCode().equals(HttpStatus.FORBIDDEN)) {
                        return Mono.error(new RuntimeException("Acceso denegado: Token inválido o sin permisos para este recurso"));
                    }
                    return response.createException().flatMap(Mono::error);
                })
                .onStatus(HttpStatusCode::is5xxServerError, response ->
                        Mono.error(new RuntimeException("Error en el servidor de la API externa"))
                )
                .bodyToMono(String.class);
    }
}
