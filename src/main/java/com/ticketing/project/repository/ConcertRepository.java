package com.ticketing.project.repository;

import com.ticketing.project.entity.Concert;
import com.ticketing.project.util.enums.ConcertStatus;
import jakarta.persistence.LockModeType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ConcertRepository extends JpaRepository<Concert, Long> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select c from Concert c where c.id = :id")
    Optional<Concert> findByIdForUpdate(@Param("id") Long concertId);

    List<Concert> findAllByStatusNotIn(List<ConcertStatus> statuses);

    Page<Concert> findPagedConcertsByStatusNotIn(List<ConcertStatus> statuses, Pageable pageable);
}
