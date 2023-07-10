package com.tperuch.voteservice.stub.sessionstatus;

import com.tperuch.voteservice.entity.SessionStatusEntity;

public class SessionStatusEntityStub {
    public static SessionStatusEntity getSessionStatusStub(){
        SessionStatusEntity statusEntity = new SessionStatusEntity();
        statusEntity.setSessionStatus("OPEN");
        statusEntity.setSessionId(1L);
        statusEntity.setId(1L);
        return statusEntity;
    }
}
