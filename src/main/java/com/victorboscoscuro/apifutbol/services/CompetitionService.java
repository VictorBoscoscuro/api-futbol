package com.victorboscoscuro.apifutbol.services;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;


@Service
public class CompetitionService {

    private final WebClient webClient;

    public CompetitionService(WebClient webClient) {
        this.webClient = webClient;
    }

    public Mono<String> getStandings(String competitionId) {
        return webClient.get()
                .uri("/competitions/{competitionId}/standings", competitionId)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, response -> {
                    if (response.statusCode().equals(HttpStatus.FORBIDDEN)) {
                        return Mono.error(new RuntimeException("Acceso denegado: Token inválido o sin permisos"));
                    }
                    return response.createException().flatMap(Mono::error);
                })
                .onStatus(HttpStatusCode::is5xxServerError, response ->
                        Mono.error(new RuntimeException("Error en el servidor de la API externa"))
                )
                .bodyToMono(String.class);
    }

}
