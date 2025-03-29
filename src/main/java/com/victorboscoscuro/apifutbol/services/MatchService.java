package com.victorboscoscuro.apifutbol.services;

import org.springframework.cglib.core.Local;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.LocalDate;

@Service
public class MatchService {

    private final WebClient webClient;

    public MatchService(WebClient webClient) {
        this.webClient = webClient;
    }

    public Mono<String> getMatch(Integer matchId) {
        return webClient.get()
                .uri("/matches/{matchId}", matchId)
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

    public Mono<String> getMatchesBetweenDates(LocalDate dateFrom, LocalDate dateTo) {

        if(dateFrom.isBefore(LocalDate.now().minusDays(1) )){
            return Mono.error(new RuntimeException("La fecha de inicio debe ser posterior a la fecha de ayer"));
        }

        return webClient.get()
                .uri(uriBuilder -> uriBuilder.path("/matches")
                        .queryParam("dateFrom", dateFrom).queryParam("dateTo", dateTo)
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
}
