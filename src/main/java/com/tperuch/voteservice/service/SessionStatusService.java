package com.tperuch.voteservice.service;

import com.tperuch.voteservice.repository.SessionStatusRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SessionStatusService {

    @Autowired
    private SessionStatusRepository statusRepository;

    public boolean isSessionOpenForVoting(Long sessionId){
        return statusRepository.existsBySessionIdAndSessionStatus(sessionId, true);
    }
}
