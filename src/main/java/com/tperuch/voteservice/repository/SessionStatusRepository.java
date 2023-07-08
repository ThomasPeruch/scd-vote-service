package com.tperuch.voteservice.repository;

import com.tperuch.voteservice.entity.SessionStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SessionStatusRepository extends JpaRepository<SessionStatus, Long> {
    boolean existsBySessionIdAndSessionStatus(Long sessionId, boolean sessionStatus);
}
