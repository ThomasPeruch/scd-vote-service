package com.tperuch.voteservice.stub.vote;

import com.tperuch.voteservice.entity.VoteEntity;

public class VoteEntityStub {
    public static VoteEntity getVoteEntityStub(){
        VoteEntity voteEntity = new VoteEntity();
        voteEntity.setVote(true);
        voteEntity.setSessionId(1L);
        voteEntity.setAssociateId(1L);
        voteEntity.setId(1L);
        return voteEntity;
    }
}
