package com.victorboscoscuro.apifutbol.resources;

import com.victorboscoscuro.apifutbol.services.CompetitionService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/competition")
public class CompetitionController {

    private final CompetitionService competitionService;

    public CompetitionController(CompetitionService competitionService) {
        this.competitionService = competitionService;
    }

    @GetMapping("/standings/{competitionId}")
    public Mono<ResponseEntity<String>> getFootballData(@PathVariable String competitionId) {
        return competitionService.getStandings(competitionId)
                .map(response -> ResponseEntity.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(response))
                .onErrorResume(RuntimeException.class, e ->
                        Mono.just(ResponseEntity.status(403).body("Error: " + e.getMessage()))
                )
                .onErrorResume(Exception.class, e ->
                        Mono.just(ResponseEntity.status(500).body("Error interno: " + e.getMessage()))
                );
    }
}
