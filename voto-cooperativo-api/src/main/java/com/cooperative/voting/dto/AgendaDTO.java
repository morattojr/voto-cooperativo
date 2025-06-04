package com.cooperative.voting.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class AgendaDTO {
  private Long id;
  private String title;
  private String description;
  private LocalDateTime createdAt;
  private LocalDateTime votingSessionStart;
  private LocalDateTime votingSessionEnd;
  private boolean votingSessionOpen;
}
