package com.cooperative.voting.model;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "vote")
@EntityListeners(AuditingEntityListener.class)
public class Vote {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "agenda_id", nullable = false)
  private Agenda agenda;

  @Column(name = "user_id", nullable = false, length = 11)
  private String userId;

  @Column(name = "vote_value", nullable = false, length = 3)
  private String voteValue;

  @CreatedDate
  @Column(name = "created_at", nullable = false, updatable = false)
  private LocalDateTime createdAt;
}
