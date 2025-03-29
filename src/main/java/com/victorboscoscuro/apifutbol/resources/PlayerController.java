package com.victorboscoscuro.apifutbol.resources;

import com.victorboscoscuro.apifutbol.services.CompetitionService;
import com.victorboscoscuro.apifutbol.services.PlayerService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/player")
public class PlayerController {

    private final PlayerService playerService;

    public PlayerController(PlayerService playerService) {
        this.playerService = playerService;
    }

    @GetMapping("/{playerId}")
    public Mono<ResponseEntity<String>> getPlayer(@PathVariable Integer playerId) {
        return playerService.getPlayer(playerId)
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
