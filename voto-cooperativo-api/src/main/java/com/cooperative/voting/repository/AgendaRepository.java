package com.cooperative.voting.repository;

import com.cooperative.voting.model.Agenda;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AgendaRepository extends JpaRepository<Agenda, Long> {
}

