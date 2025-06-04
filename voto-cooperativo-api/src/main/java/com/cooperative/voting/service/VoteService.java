package com.cooperative.voting.service;

import com.cooperative.voting.client.UserValidationClient;
import com.cooperative.voting.client.dto.UserDTO;
import com.cooperative.voting.dto.VoteDTO;
import com.cooperative.voting.exception.VotingException;
import com.cooperative.voting.model.Agenda;
import com.cooperative.voting.model.Vote;
import com.cooperative.voting.model.VoteOption;
import com.cooperative.voting.repository.VoteRepository;
import com.userservice.model.UserStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class VoteService {

    private final AgendaService agendaService;
    private final VoteRepository voteRepository;
    private final UserValidationClient userValidationClient;

    @Transactional
    public VoteDTO castVote(Long agendaId, VoteDTO voteDTO) {
        Agenda agenda = agendaService.findAgenda(agendaId);

        validarSessaoAberta(agenda);
        validarVotoDuplicado(agendaId, voteDTO.getUserId());
        validarUsuarioHabilitado(voteDTO.getUserId());

        Vote vote = Vote.builder()
                .agenda(agenda)
                .userId(voteDTO.getUserId())
                .voteValue(voteDTO.getVoteValue().name())
                .build();

        Vote saved = voteRepository.save(vote);
        return toVoteDTO(saved);
    }

    private void validarSessaoAberta(Agenda agenda) {
        if (!agenda.isVotingSessionOpen() ||
                agenda.getVotingSessionEnd() == null ||
                LocalDateTime.now().isAfter(agenda.getVotingSessionEnd())) {
            throw new VotingException("Sessão de votação está fechada para a pauta " + agenda.getId(), 400);
        }
    }

    private void validarVotoDuplicado(Long agendaId, String userId) {
        if (voteRepository.findByAgendaIdAndUserId(agendaId, userId).isPresent()) {
            throw new VotingException("Usuário " + userId + " já votou na pauta " + agendaId, 400);
        }
    }

    private void validarUsuarioHabilitado(String userId) {
        UserDTO user = userValidationClient.getUser(userId);
        if (user == null || user.getStatus() != UserStatus.ABLE_TO_VOTE) {
            throw new VotingException("Usuário " + userId + " não está habilitado a votar", 400);
        }
    }

    private VoteDTO toVoteDTO(Vote vote) {
        return VoteDTO.builder()
                .id(vote.getId())
                .agendaId(vote.getAgenda().getId())
                .userId(vote.getUserId())
                .voteValue(VoteOption.valueOf(vote.getVoteValue()))
                .createdAt(vote.getCreatedAt())
                .build();
    }
}
