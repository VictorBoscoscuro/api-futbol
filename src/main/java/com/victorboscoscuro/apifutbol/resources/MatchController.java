package com.victorboscoscuro.apifutbol.resources;

import com.victorboscoscuro.apifutbol.services.MatchService;
import com.victorboscoscuro.apifutbol.services.PlayerService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.time.LocalDate;

@RestController
@RequestMapping("/matches")
public class MatchController {

    private final MatchService matchService;

    public MatchController(MatchService matchService) {
        this.matchService = matchService;
    }

    @GetMapping("/{matchId}")
    public Mono<ResponseEntity<String>> getMatch(@PathVariable Integer matchId) {
        return matchService.getMatch(matchId)
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

    @GetMapping("")
    public Mono<ResponseEntity<String>> getMatches(@RequestParam(required = true) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateFrom, @RequestParam(required = true) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateTo) {
        return matchService.getMatchesBetweenDates(dateFrom, dateTo)
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
