package com.cooperative.voting.controller;

import com.cooperative.voting.config.StandardApiResponses;
import com.cooperative.voting.dto.AgendaDTO;
import com.cooperative.voting.dto.OpenSessionRequest;
import com.cooperative.voting.dto.VotingResultDTO;
import com.cooperative.voting.service.AgendaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/agendas")
@Tag(name = "Pautas", description = "Gerenciar pautas e sessões de votação")
public class AgendaController {

    private final AgendaService agendaService;

    public AgendaController(AgendaService agendaService) {
        this.agendaService = agendaService;
    }

    @Operation(
            summary = "Criar uma nova pauta",
            description = "Cria uma pauta para votação na plataforma."
    )
    @ApiResponse(responseCode = "201", description = "Pauta criada com sucesso")
    @ApiResponse(responseCode = "400", description = "Dados inválidos para criação da pauta")
    @StandardApiResponses
    @PostMapping
    public ResponseEntity<AgendaDTO> createAgenda(@Valid @RequestBody AgendaDTO request) {
        AgendaDTO created = agendaService.createAgenda(request);
        return ResponseEntity.status(201).body(created);
    }

    @Operation(
            summary = "Abrir uma sessão de votação",
            description = "Abre uma sessão de votação para a pauta informada. Duração padrão é 1 minuto se não informado."
    )
    @ApiResponse(responseCode = "200", description = "Sessão de votação aberta com sucesso")
    @ApiResponse(responseCode = "400", description = "Dados inválidos para abertura da sessão")
    @ApiResponse(responseCode = "404", description = "Pauta não encontrada")
    @StandardApiResponses
    @PostMapping("/{agendaId}/sessions")
    public ResponseEntity<AgendaDTO> openVotingSession(@PathVariable Long agendaId,
                                                       @Valid @RequestBody(required = false) OpenSessionRequest request) {
        OpenSessionRequest sessionRequest = request != null ? request : new OpenSessionRequest();
        AgendaDTO updated = agendaService.openVotingSession(agendaId, sessionRequest);
        return ResponseEntity.ok(updated);
    }

    @Operation(
            summary = "Obter resultados da votação",
            description = "Retorna os resultados da votação da pauta informada."
    )
    @ApiResponse(responseCode = "200", description = "Resultados retornados com sucesso")
    @ApiResponse(responseCode = "404", description = "Pauta não encontrada")
    @StandardApiResponses
    @GetMapping("/{agendaId}/results")
    public ResponseEntity<VotingResultDTO> getVotingResults(@PathVariable Long agendaId) {
        VotingResultDTO results = agendaService.getVotingResults(agendaId);
        return ResponseEntity.ok(results);
    }

    @Operation(
            summary = "Fechar a pauta (sessão de votação)",
            description = "Encerra a sessão de votação da pauta informada."
    )
    @ApiResponse(responseCode = "204", description = "Sessão de votação encerrada com sucesso")
    @ApiResponse(responseCode = "400", description = "Pauta já está fechada ou dados inválidos")
    @ApiResponse(responseCode = "404", description = "Pauta não encontrada")
    @StandardApiResponses
    @PostMapping("/{agendaId}/close")
    public ResponseEntity<Void> closeAgenda(@PathVariable Long agendaId) {
        agendaService.closeAgenda(agendaId);
        return ResponseEntity.noContent().build();
    }
}
