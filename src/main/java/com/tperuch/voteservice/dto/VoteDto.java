package com.tperuch.voteservice.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class VoteDto {
    @NotNull(message = "id da sessão deve ser informado")
    private Long idSession;
    @Positive(message = "o id do associado deve ser positivo e maior que zero")
    private Long idAssociate;
    @NotNull(message = "voto deve ser informado")
    private Boolean vote;

    public Long getIdSession() {
        return idSession;
    }

    public void setIdSession(Long idSession) {
        this.idSession = idSession;
    }

    public Long getIdAssociate() {
        return idAssociate;
    }

    public void setIdAssociate(Long idAssociate) {
        this.idAssociate = idAssociate;
    }

    public Boolean isVote() {
        return vote;
    }

    public void setVote(Boolean vote) {
        this.vote = vote;
    }
}
