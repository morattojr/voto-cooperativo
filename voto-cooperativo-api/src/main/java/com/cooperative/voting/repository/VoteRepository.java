package com.cooperative.voting.repository;

import com.cooperative.voting.model.Vote;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface VoteRepository extends JpaRepository<Vote, Long> {
  Optional<Vote> findByAgendaIdAndUserId(Long agendaId, String userId);
  long countByAgendaIdAndVoteValue(Long agendaId, String voteValue);
  long countByAgendaId(Long agendaId);
}
