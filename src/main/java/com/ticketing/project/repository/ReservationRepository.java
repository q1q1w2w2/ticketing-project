package com.ticketing.project.repository;

import com.ticketing.project.entity.Concert;
import com.ticketing.project.entity.Reservation;
import com.ticketing.project.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    Optional<Reservation> findByUserAndConcertAndStatus(User user, Concert concert, int status);
}
