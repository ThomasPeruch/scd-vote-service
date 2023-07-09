package com.tperuch.voteservice.dto.response;

public class VoteResultResponseDto {

    private Long sessionId;
    private Integer votesForYes;
    private Integer votesForNo;
    private String result;

    public Long getSessionId() {
        return sessionId;
    }

    public void setSessionId(Long sessionId) {
        this.sessionId = sessionId;
    }

    public Integer getVotesForYes() {
        return votesForYes;
    }

    public void setVotesForYes(Integer votesForYes) {
        this.votesForYes = votesForYes;
    }

    public Integer getVotesForNo() {
        return votesForNo;
    }

    public void setVotesForNo(Integer votesForNo) {
        this.votesForNo = votesForNo;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }
}
