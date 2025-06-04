package com.cooperative.voting.service;

import com.cooperative.voting.dto.AgendaDTO;
import com.cooperative.voting.dto.OpenSessionRequest;
import com.cooperative.voting.dto.VotingResultDTO;
import com.cooperative.voting.exception.VotingException;
import com.cooperative.voting.model.Agenda;
import com.cooperative.voting.model.VoteOption;
import com.cooperative.voting.repository.AgendaRepository;
import com.cooperative.voting.repository.VoteRepository;
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
class AgendaServiceTest {

    @Mock
    private AgendaRepository agendaRepository;

    @Mock
    private VoteRepository voteRepository;

    @InjectMocks
    private AgendaService agendaService;

    private Agenda agenda;
    private AgendaDTO agendaDTO;
    private LocalDateTime now;

    @BeforeEach
    void setUp() {
        now = LocalDateTime.now();
        agendaDTO = AgendaDTO.builder()
                .title("Test Topic")
                .description("Test Description")
                .build();

        agenda = Agenda.builder()
                .id(1L)
                .title("Test Topic")
                .description("Test Description")
                .createdAt(now.minusDays(1))
                .votingSessionOpen(false)
                .build();
    }

    @Test
    @DisplayName("Should create agenda successfully")
    void createAgenda_Success() {
        when(agendaRepository.save(any(Agenda.class))).thenReturn(agenda);

        AgendaDTO result = agendaService.createAgenda(agendaDTO);

        assertNotNull(result);
        assertEquals(agenda.getId(), result.getId());
        assertEquals(agendaDTO.getTitle(), result.getTitle());
        verify(agendaRepository, times(1)).save(any(Agenda.class));
    }

    @Test
    @DisplayName("Should open voting session successfully with default duration")
    void openVotingSession_DefaultDuration_Success() {
        OpenSessionRequest request = new OpenSessionRequest();
        agenda.setVotingSessionStart(null);
        Agenda updatedAgenda = Agenda.builder()
                .id(1L)
                .title(agenda.getTitle())
                .description(agenda.getDescription())
                .createdAt(agenda.getCreatedAt())
                .votingSessionStart(now)
                .votingSessionEnd(now.plusMinutes(1))
                .votingSessionOpen(true)
                .build();

        when(agendaRepository.findById(anyLong())).thenReturn(Optional.of(agenda));
        when(agendaRepository.save(any(Agenda.class))).thenReturn(updatedAgenda);

        AgendaDTO result = agendaService.openVotingSession(1L, request);

        assertNotNull(result);
        assertTrue(result.isVotingSessionOpen());
        assertNotNull(result.getVotingSessionStart());
        assertNotNull(result.getVotingSessionEnd());
        assertTrue(result.getVotingSessionEnd().isAfter(result.getVotingSessionStart()));
        verify(agendaRepository, times(1)).findById(1L);
        verify(agendaRepository, times(1)).save(any(Agenda.class));
    }

    @Test
    @DisplayName("Should open voting session successfully with specified duration")
    void openVotingSession_SpecifiedDuration_Success() {
        OpenSessionRequest request = new OpenSessionRequest();
        request.setDurationMinutes(5);
        agenda.setVotingSessionStart(null);
        Agenda updatedAgenda = Agenda.builder()
                .id(1L)
                .title(agenda.getTitle())
                .description(agenda.getDescription())
                .createdAt(agenda.getCreatedAt())
                .votingSessionStart(now)
                .votingSessionEnd(now.plusMinutes(5))
                .votingSessionOpen(true)
                .build();

        when(agendaRepository.findById(anyLong())).thenReturn(Optional.of(agenda));
        when(agendaRepository.save(any(Agenda.class))).thenReturn(updatedAgenda);

        AgendaDTO result = agendaService.openVotingSession(1L, request);

        assertNotNull(result);
        assertTrue(result.isVotingSessionOpen());
        assertNotNull(result.getVotingSessionStart());
        assertNotNull(result.getVotingSessionEnd());
        assertTrue(result.getVotingSessionEnd().isAfter(result.getVotingSessionStart().plusMinutes(4)));
        verify(agendaRepository, times(1)).findById(1L);
        verify(agendaRepository, times(1)).save(any(Agenda.class));
    }

    @Test
    @DisplayName("Should throw VotingException when opening already open session")
    void openVotingSession_AlreadyOpen_ThrowsException() {
        agenda.setVotingSessionStart(now.minusMinutes(1));
        when(agendaRepository.findById(anyLong())).thenReturn(Optional.of(agenda));

        VotingException exception = assertThrows(VotingException.class,
                () -> agendaService.openVotingSession(1L, new OpenSessionRequest()));

        assertEquals(400, exception.getStatusCode());
        verify(agendaRepository, times(1)).findById(1L);
        verify(agendaRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should return voting results")
    void getVotingResults_Success() {
        when(agendaRepository.findById(anyLong())).thenReturn(Optional.of(agenda));
        when(voteRepository.countByAgendaIdAndVoteValue(1L, VoteOption.YES.name())).thenReturn(3L);
        when(voteRepository.countByAgendaIdAndVoteValue(1L, VoteOption.NO.name())).thenReturn(2L);

        VotingResultDTO result = agendaService.getVotingResults(1L);

        assertNotNull(result);
        assertEquals(3L, result.getYesVotes());
        assertEquals(2L, result.getNoVotes());
        assertEquals(5L, result.getTotalVotes());
        assertEquals("Aprovado", result.getResult());
        verify(agendaRepository, times(1)).findById(1L);
        verify(voteRepository, times(1)).countByAgendaIdAndVoteValue(1L, VoteOption.YES.name());
        verify(voteRepository, times(1)).countByAgendaIdAndVoteValue(1L, VoteOption.NO.name());
    }

    @Test
    @DisplayName("Deve fechar sessão de votação com sucesso")
    void closeVotingSession_Success() {
        agenda.setVotingSessionOpen(true);
        agenda.setVotingSessionStart(now.minusMinutes(10));
        agenda.setVotingSessionEnd(now.minusMinutes(1));

        when(agendaRepository.findById(1L)).thenReturn(Optional.of(agenda));
        when(agendaRepository.save(any(Agenda.class))).thenReturn(agenda);

        AgendaDTO result = agendaService.closeVotingSession(1L);

        assertNotNull(result);
        assertFalse(result.isVotingSessionOpen());
        verify(agendaRepository, times(1)).findById(1L);
        verify(agendaRepository, times(1)).save(any(Agenda.class));
    }

    @Test
    @DisplayName("Deve lançar exceção ao tentar fechar uma sessão que já está fechada")
    void closeVotingSession_AlreadyClosed_ThrowsException() {
        agenda.setVotingSessionOpen(false);
        when(agendaRepository.findById(1L)).thenReturn(Optional.of(agenda));

        VotingException exception = assertThrows(VotingException.class,
                () -> agendaService.closeVotingSession(1L));

        assertEquals("Sessão de votação já está fechada", exception.getMessage());
        assertEquals(400, exception.getStatusCode());
        verify(agendaRepository, times(1)).findById(1L);
        verify(agendaRepository, never()).save(any());
    }

    @Test
    @DisplayName("Deve lançar exceção ao tentar fechar uma sessão ainda em andamento")
    void closeVotingSession_NotFinishedYet_ThrowsException() {
        agenda.setVotingSessionOpen(true);
        agenda.setVotingSessionStart(now.minusMinutes(1));
        agenda.setVotingSessionEnd(now.plusMinutes(5));
        when(agendaRepository.findById(1L)).thenReturn(Optional.of(agenda));

        VotingException exception = assertThrows(VotingException.class,
                () -> agendaService.closeVotingSession(1L));

        assertEquals("Sessão de votação ainda está em andamento", exception.getMessage());
        assertEquals(400, exception.getStatusCode());
        verify(agendaRepository, times(1)).findById(1L);
        verify(agendaRepository, never()).save(any());
    }

    @Test
    @DisplayName("Deve lançar exceção ao tentar fechar sessão de pauta inexistente")
    void closeVotingSession_NotFound_ThrowsException() {
        when(agendaRepository.findById(1L)).thenReturn(Optional.empty());

        VotingException exception = assertThrows(VotingException.class,
                () -> agendaService.closeVotingSession(1L));

        assertEquals("Pauta não encontrada", exception.getMessage());
        assertEquals(404, exception.getStatusCode());
        verify(agendaRepository, times(1)).findById(1L);
        verify(agendaRepository, never()).save(any());
    }

}
