package com.cooperative.voting.model;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "agenda")
@EntityListeners(AuditingEntityListener.class)
public class Agenda {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "description")
    private String description;

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "voting_session_start")
    private LocalDateTime votingSessionStart;

    @Column(name = "voting_session_end")
    private LocalDateTime votingSessionEnd;

    @Column(name = "voting_session_open")
    private boolean votingSessionOpen;


}
