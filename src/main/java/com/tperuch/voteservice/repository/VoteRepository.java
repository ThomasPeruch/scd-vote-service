package com.tperuch.voteservice.repository;

import com.tperuch.voteservice.entity.VoteEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VoteRepository extends JpaRepository<VoteEntity, Long> {
    boolean existsBySessionIdAndAssociateId(Long sessionId, Long associateId);

    List<VoteEntity> findBySessionId(Long sessionId);
}
