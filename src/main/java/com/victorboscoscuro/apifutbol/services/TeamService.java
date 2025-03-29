package com.victorboscoscuro.apifutbol.services;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class TeamService {

    private final WebClient webClient;

    public TeamService(WebClient webClient) {
        this.webClient = webClient;
    }

    public Mono<String> getTeams(Integer limit, Integer offset) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder.path("/teams")
                        .queryParam("limit", limit).queryParam("offset", offset)
                        .build())
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

    public Mono<String> getTeam(Integer teamId) {
        return webClient.get()
                .uri("/teams/{teamId}", teamId)
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
