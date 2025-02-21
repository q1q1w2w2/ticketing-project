package com.ticketing.project.repository;

import com.ticketing.project.entity.ReservationFailureLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReservationFailureLogRepository extends JpaRepository<ReservationFailureLog, Long> {
}
