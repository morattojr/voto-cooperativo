package com.cooperative.voting.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class VotingResultDTO {
  private Long agendaId;
  private String agendaTitle;
  private long yesVotes;
  private long noVotes;
  private long totalVotes;
  private String result;
  private boolean sessionClosed;
}
