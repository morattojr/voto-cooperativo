package com.cooperative.voting.controller;

import com.cooperative.voting.config.StandardApiResponses;
import com.cooperative.voting.dto.VoteDTO;
import com.cooperative.voting.service.VoteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/agendas/{agendaId}/votes")
@Tag(name = "Votos", description = "Gerencia votos")
public class VoteController {

    private final VoteService voteService;

    public VoteController(VoteService voteService) {
        this.voteService = voteService;
    }

    @Operation(
            summary = "Registrar um voto",
            description = "Permite que um usuário registre um voto em uma pauta específica."
    )
    @ApiResponse(responseCode = "201", description = "Voto criado com sucesso")
    @ApiResponse(responseCode = "400", description = "Voto inválido (ex.: CPF inválido, já votou, sessão fechada)")
    @ApiResponse(responseCode = "404", description = "Pauta não encontrada")
    @StandardApiResponses
    @PostMapping
    public ResponseEntity<VoteDTO> castVote(@PathVariable Long agendaId, @Valid @RequestBody VoteDTO vote) {
        VoteDTO created = voteService.castVote(agendaId, vote);
        return ResponseEntity.status(201).body(created);
    }
}
