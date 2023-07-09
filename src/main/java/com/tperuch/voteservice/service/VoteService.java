package com.tperuch.voteservice.service;

import com.tperuch.voteservice.dto.VoteDto;
import com.tperuch.voteservice.dto.response.VoteResultResponseDto;
import com.tperuch.voteservice.entity.VoteEntity;
import com.tperuch.voteservice.repository.VoteRepository;
import jakarta.persistence.EntityNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VoteService {
    @Autowired
    private SessionStatusService sessionStatusService;
    @Autowired
    private VoteRepository voteRepository;
    @Autowired
    private ModelMapper modelMapper;

    public VoteDto saveVote(VoteDto voteDto){
        if(!isSessionOpen(voteDto.getIdSession())){
            throw new EntityNotFoundException("A sessão informada não esta aberta para votação, escolha outra");
        }
        if(associateAlreadyVotedInSession(voteDto.getIdSession(), voteDto.getIdAssociate())){
            throw new IllegalArgumentException(
                    "O associado informado ja votou nessa sessão de votação - ID do associado "+voteDto.getIdAssociate()+ " - ID da sessão "+voteDto.getIdSession());
        }
        VoteEntity entity = buildVoteEntity(voteDto);
        VoteEntity savedEntity = voteRepository.save(entity);
        return modelMapper.map(savedEntity, VoteDto.class);
    }

    private boolean isSessionOpen(Long sessionId) {
        return sessionStatusService.isSessionOpen(sessionId);
    }

    public VoteResultResponseDto getVotesAndSessionResult(Long sessionId){
        if(isSessionOpen(sessionId)){
            throw new IllegalArgumentException("Essa votação segue em aberto, para visualizar o resultado selecione uma já encerrada");
        }
        return getVotesFromSession(sessionId);
    }

    private VoteResultResponseDto getVotesFromSession(Long sessionId) {
        List<VoteEntity> votes = voteRepository.findBySessionId(sessionId);
        return buildResultDto(votes, sessionId);
    }

    private VoteResultResponseDto buildResultDto(List<VoteEntity> votes, Long sessionId) {
        int votesForYes = countVotesForYes(votes);
        int votesForNo = countVotesForNo(votes);
        VoteResultResponseDto voteResultResponseDto = new VoteResultResponseDto();
        voteResultResponseDto.setVotesForYes(votesForYes);
        voteResultResponseDto.setVotesForNo(votesForNo);
        voteResultResponseDto.setSessionId(sessionId);
        voteResultResponseDto.setResult(getResult(votesForYes, votesForNo));
        return voteResultResponseDto;
    }

    private String getResult(int votesForYes, int votesForNo) {
        int result = Integer.compare(votesForYes, votesForNo);
        return switch (result) {
            case 0 -> "EMPATE";
            case 1 -> "REJEITADA";
            default -> "APROVADA";
        };
    }

    private int countVotesForYes(List<VoteEntity> votes) {
        return countVotes(votes, true);
    }

    private int countVotesForNo(List<VoteEntity> votes) {
        return countVotes(votes, false);
    }

    private int countVotes(List<VoteEntity> votes, boolean value) {
        return votes.stream().filter(vote -> vote.isVote() == value).toList().size();
    }

    private boolean associateAlreadyVotedInSession(Long idSession, Long idAssociate) {
        return voteRepository.existsBySessionIdAndAssociateId(idSession, idAssociate);
    }

    private VoteEntity buildVoteEntity(VoteDto voteDto) {
        VoteEntity voteEntity = new VoteEntity();
        voteEntity.setAssociateId(voteDto.getIdAssociate());
        voteEntity.setSessionId(voteDto.getIdSession());
        voteEntity.setVote(voteDto.isVote());
        return voteEntity;
    }
}
