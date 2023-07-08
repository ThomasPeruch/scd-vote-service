package com.tperuch.voteservice.repository;

import com.tperuch.voteservice.entity.SessionStatusEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SessionStatusRepository extends JpaRepository<SessionStatusEntity, Long> {
    boolean existsBySessionIdAndSessionStatus(Long sessionId, boolean sessionStatus);
}
