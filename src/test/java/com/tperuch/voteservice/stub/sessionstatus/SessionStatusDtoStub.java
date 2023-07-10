package com.tperuch.voteservice.stub.sessionstatus;

import com.tperuch.voteservice.dto.SessionStatusDto;

public class SessionStatusDtoStub {
    public static SessionStatusDto getSessionStatusDto(){
        SessionStatusDto sessionStatusDto = new SessionStatusDto();
        sessionStatusDto.setSessionId(1L);
        sessionStatusDto.setStatus("OPEN");
        return sessionStatusDto;
    }
}
