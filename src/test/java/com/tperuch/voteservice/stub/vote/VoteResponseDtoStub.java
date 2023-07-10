package com.tperuch.voteservice.stub.vote;

import com.tperuch.voteservice.dto.response.VoteResponseDto;

public class VoteResponseDtoStub {
    public static VoteResponseDto getVoteResponseDtoStub(){
        VoteResponseDto responseDto = new VoteResponseDto();
        responseDto.setId(1L);
        responseDto.setVote(true);
        responseDto.setAssociateId(1L);
        responseDto.setSessionId(1L);
        return responseDto;
    }

}
