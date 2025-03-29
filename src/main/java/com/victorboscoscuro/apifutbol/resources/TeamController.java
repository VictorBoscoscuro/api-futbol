package com.victorboscoscuro.apifutbol.resources;

import com.victorboscoscuro.apifutbol.services.CompetitionService;
import com.victorboscoscuro.apifutbol.services.TeamService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.time.LocalDate;

@RestController
@RequestMapping("/team")
public class TeamController {

    private final TeamService teamService;

    public TeamController(TeamService teamService) {
        this.teamService = teamService;
    }

    @GetMapping("")
    public Mono<ResponseEntity<String>> getTeams(@RequestParam(required = true) Integer limit, @RequestParam(required = true) Integer offset) {
        return teamService.getTeams(limit, offset)
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

    @GetMapping("/{teamId}")
    public Mono<ResponseEntity<String>> getTeam(@PathVariable Integer teamId) {
        return teamService.getTeam(teamId)
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
