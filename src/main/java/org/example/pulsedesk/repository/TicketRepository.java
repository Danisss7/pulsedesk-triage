package org.example.pulsedesk.repository;

import org.example.pulsedesk.model.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TicketRepository extends JpaRepository<Ticket, Long> {
    Optional<Ticket> findByCommentId(Long commentId);
}