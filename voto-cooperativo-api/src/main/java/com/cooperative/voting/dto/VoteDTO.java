package com.cooperative.voting.dto;

import com.cooperative.voting.model.VoteOption;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class VoteDTO {
  private Long id;
  private Long agendaId;
  @NotBlank(message = "O CPF é obrigatório")
  @Size(min = 11, max = 11, message = "O CPF deve conter 11 dígitos")
  private String userId;
  private VoteOption voteValue;
  private LocalDateTime createdAt;
}