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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class VoteServiceTest {

    @Mock
    private AgendaService agendaService;

    @Mock
    private VoteRepository voteRepository;

    @Mock
    private UserValidationClient userValidationClient;

    @InjectMocks
    private VoteService voteService;

    private Agenda openAgenda;
    private Agenda closedAgenda;
    private VoteDTO voteDTO;
    private Vote vote;
    private LocalDateTime now;

    @BeforeEach
    void setUp() {
        now = LocalDateTime.now();

        openAgenda = Agenda.builder()
                .id(1L)
                .title("Open Agenda")
                .votingSessionOpen(true)
                .votingSessionStart(now.minusMinutes(10))
                .votingSessionEnd(now.plusMinutes(10))
                .build();

        closedAgenda = Agenda.builder()
                .id(2L)
                .title("Closed Agenda")
                .votingSessionOpen(false)
                .votingSessionStart(now.minusMinutes(20))
                .votingSessionEnd(now.minusMinutes(10))
                .build();

        voteDTO = VoteDTO.builder()
                .userId("12345678901")
                .voteValue(VoteOption.YES)
                .build();

        vote = Vote.builder()
                .id(1L)
                .agenda(openAgenda)
                .userId("12345678901")
                .voteValue(VoteOption.YES.name())
                .createdAt(now)
                .build();
    }

    private UserDTO createUserDTOWithStatus(UserStatus status) {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(voteDTO.getUserId());
        userDTO.setStatus(status);
        return userDTO;
    }

    @Test
    @DisplayName("Should cast vote successfully")
    void castVote_Success() {
        when(agendaService.findAgenda(1L)).thenReturn(openAgenda);
        when(voteRepository.findByAgendaIdAndUserId(1L, voteDTO.getUserId())).thenReturn(Optional.empty());
        when(userValidationClient.getUser(voteDTO.getUserId()))
                .thenReturn(createUserDTOWithStatus(UserStatus.ABLE_TO_VOTE));
        when(voteRepository.save(any(Vote.class))).thenReturn(vote);

        VoteDTO result = voteService.castVote(1L, voteDTO);

        assertNotNull(result);
        assertEquals(vote.getId(), result.getId());
        assertEquals(voteDTO.getUserId(), result.getUserId());
        assertEquals(voteDTO.getVoteValue(), result.getVoteValue());
    }

    @Test
    @DisplayName("Should throw VotingException when agenda is not found")
    void castVote_AgendaNotFound() {
        when(agendaService.findAgenda(anyLong()))
                .thenThrow(new VotingException("Agenda not found: 99", 404));

        VotingException ex = assertThrows(VotingException.class,
                () -> voteService.castVote(99L, voteDTO));

        assertEquals("Agenda not found: 99", ex.getMessage());
    }

    @Test
    @DisplayName("Should throw VotingException when session is closed")
    void castVote_SessionClosed() {
        when(agendaService.findAgenda(2L)).thenReturn(closedAgenda);

        VotingException ex = assertThrows(VotingException.class,
                () -> voteService.castVote(2L, voteDTO));

        assertTrue(ex.getMessage().contains("Sessão de votação está fechada"));
    }

    @Test
    @DisplayName("Should throw VotingException when user already voted")
    void castVote_UserAlreadyVoted() {
        when(agendaService.findAgenda(1L)).thenReturn(openAgenda);
        when(voteRepository.findByAgendaIdAndUserId(1L, voteDTO.getUserId())).thenReturn(Optional.of(vote));

        VotingException ex = assertThrows(VotingException.class,
                () -> voteService.castVote(1L, voteDTO));

        assertTrue(ex.getMessage().contains("já votou"));
    }

    @Test
    @DisplayName("Should throw VotingException when user is UNABLE_TO_VOTE")
    void castVote_UserUnableToVote() {
        when(agendaService.findAgenda(1L)).thenReturn(openAgenda);
        when(voteRepository.findByAgendaIdAndUserId(1L, voteDTO.getUserId())).thenReturn(Optional.empty());
        when(userValidationClient.getUser(voteDTO.getUserId()))
                .thenReturn(createUserDTOWithStatus(UserStatus.UNABLE_TO_VOTE));

        VotingException ex = assertThrows(VotingException.class,
                () -> voteService.castVote(1L, voteDTO));

        assertTrue(ex.getMessage().contains("não está habilitado"));
    }
}
