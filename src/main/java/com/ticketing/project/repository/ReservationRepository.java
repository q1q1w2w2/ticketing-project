package com.ticketing.project.repository;

import com.ticketing.project.entity.Concert;
import com.ticketing.project.entity.Reservation;
import com.ticketing.project.entity.User;
import com.ticketing.project.util.enums.TicketStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    Optional<Reservation> findByUserAndConcertAndStatus(User user, Concert concert, TicketStatus status);

    List<Reservation> findAllByConcert(Concert concert);

    List<Reservation> findAllByStatus(TicketStatus status);
}
