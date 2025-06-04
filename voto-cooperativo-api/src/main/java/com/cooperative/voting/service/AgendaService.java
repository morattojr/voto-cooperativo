package com.cooperative.voting.service;

import com.cooperative.voting.dto.AgendaDTO;
import com.cooperative.voting.dto.OpenSessionRequest;
import com.cooperative.voting.dto.VotingResultDTO;
import com.cooperative.voting.exception.VotingException;
import com.cooperative.voting.model.Agenda;
import com.cooperative.voting.model.VoteOption;
import com.cooperative.voting.repository.AgendaRepository;
import com.cooperative.voting.repository.VoteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AgendaService {

    private final AgendaRepository agendaRepository;
    private final VoteRepository voteRepository;

    @Transactional
    public AgendaDTO createAgenda(AgendaDTO agendaDTO) {
        Agenda agenda = Agenda.builder()
                .title(agendaDTO.getTitle())
                .description(agendaDTO.getDescription())
                .createdAt(LocalDateTime.now())
                .votingSessionOpen(false)
                .build();

        Agenda saved = agendaRepository.save(agenda);
        return toAgendaDTO(saved);
    }

    @Transactional
    public AgendaDTO openVotingSession(Long agendaId, OpenSessionRequest request) {
        Agenda agenda = findAgenda(agendaId);

        if (agenda.getVotingSessionStart() != null) {
            throw new VotingException("Sessão de votação já está aberta para a pauta " + agendaId, 400);
        }

        LocalDateTime now = LocalDateTime.now();
        agenda.setVotingSessionStart(now);
        agenda.setVotingSessionEnd(now.plusMinutes(
                request.getDurationMinutes() != null ? request.getDurationMinutes() : 1
        ));
        agenda.setVotingSessionOpen(true);

        Agenda updated = agendaRepository.save(agenda);
        return toAgendaDTO(updated);
    }

    @Transactional
    public void closeAgenda(Long agendaId) {
        Agenda agenda = findAgenda(agendaId);

        if (!agenda.isVotingSessionOpen()) {
            throw new VotingException("Pauta já está fechada", 400);
        }

        agenda.setVotingSessionOpen(false);
        agenda.setVotingSessionEnd(LocalDateTime.now());
        agendaRepository.save(agenda);
    }

    @Transactional(readOnly = true)
    public VotingResultDTO getVotingResults(Long agendaId) {
        Agenda agenda = findAgenda(agendaId);

        long yesVotes = voteRepository.countByAgendaIdAndVoteValue(agendaId, VoteOption.YES.name());
        long noVotes = voteRepository.countByAgendaIdAndVoteValue(agendaId, VoteOption.NO.name());

        String result = yesVotes > noVotes ? "Aprovado"
                : (yesVotes < noVotes ? "Rejeitado" : "Empate");

        boolean sessionClosed = agenda.getVotingSessionEnd() != null &&
                LocalDateTime.now().isAfter(agenda.getVotingSessionEnd());

        return VotingResultDTO.builder()
                .agendaId(agendaId)
                .agendaTitle(agenda.getTitle())
                .yesVotes(yesVotes)
                .noVotes(noVotes)
                .totalVotes(yesVotes + noVotes)
                .result(result)
                .sessionClosed(sessionClosed)
                .build();
    }

    @Transactional
    public AgendaDTO closeVotingSession(Long agendaId) {
        Agenda agenda = agendaRepository.findById(agendaId)
                .orElseThrow(() -> new VotingException("Pauta não encontrada", 404));

        if (!agenda.isVotingSessionOpen()) {
            throw new VotingException("Sessão de votação já está fechada", 400);
        }

        if (agenda.getVotingSessionEnd() == null || agenda.getVotingSessionEnd().isAfter(LocalDateTime.now())) {
            throw new VotingException("Sessão de votação ainda está em andamento", 400);
        }

        agenda.setVotingSessionOpen(false);
        Agenda updated = agendaRepository.save(agenda);
        return toAgendaDTO(updated); // ✅ Alteração feita aqui
    }

    Agenda findAgenda(Long agendaId) {
        return agendaRepository.findById(agendaId)
                .orElseThrow(() -> new VotingException("Pauta não encontrada: " + agendaId, 404));
    }

    private AgendaDTO toAgendaDTO(Agenda agenda) {
        return AgendaDTO.builder()
                .id(agenda.getId())
                .title(agenda.getTitle())
                .description(agenda.getDescription())
                .createdAt(agenda.getCreatedAt())
                .votingSessionStart(agenda.getVotingSessionStart())
                .votingSessionEnd(agenda.getVotingSessionEnd())
                .votingSessionOpen(agenda.isVotingSessionOpen())
                .build();
    }
}
