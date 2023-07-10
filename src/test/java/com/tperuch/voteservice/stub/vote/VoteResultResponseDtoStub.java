package com.tperuch.voteservice.stub.vote;

import com.tperuch.voteservice.dto.response.VoteResultResponseDto;

public class VoteResultResponseDtoStub {
    public static VoteResultResponseDto getVoteResultResponseDtoStub(){
        VoteResultResponseDto voteResultResponseDto = new VoteResultResponseDto();
        voteResultResponseDto.setVotesForYes(1);
        voteResultResponseDto.setVotesForNo(0);
        voteResultResponseDto.setSessionId(1L);
        voteResultResponseDto.setResult("APROVADA");
        return voteResultResponseDto;
    }
}
