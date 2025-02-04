package com.ticketing.project.repository;

import com.ticketing.project.entity.Ticket;
import com.ticketing.project.util.enums.TicketStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Long> {
    List<Ticket> findAllByStatus(TicketStatus status);
    Optional<Ticket> findBySerialNumber(String serialNumber);
}
