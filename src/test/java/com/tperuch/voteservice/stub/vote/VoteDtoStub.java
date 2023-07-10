package com.tperuch.voteservice.stub.vote;

import com.tperuch.voteservice.dto.VoteDto;

public class VoteDtoStub {
    public static VoteDto getVoteDtoStub(){
        VoteDto voteDto = new VoteDto();
        voteDto.setVote(true);
        voteDto.setIdSession(1L);
        voteDto.setIdAssociate(1L);
        return voteDto;
    }
}
