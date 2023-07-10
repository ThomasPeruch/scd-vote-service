package com.tperuch.voteservice.stub.sessionstatus;

import com.tperuch.voteservice.dto.response.SessionStatusResponseDto;

public class SessionStatusResponseDtoStub {
    public static SessionStatusResponseDto getSessionStatusResponseDtoStub(){
        SessionStatusResponseDto sessionStatusResponseDto = new SessionStatusResponseDto();
        sessionStatusResponseDto.setId(1L);
        sessionStatusResponseDto.setIdSession(1L);
        sessionStatusResponseDto.setSessionStatus("ABERTA");
        return sessionStatusResponseDto;
    }
}
