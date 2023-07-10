package com.tperuch.voteservice.service;

import com.tperuch.voteservice.dto.VoteDto;
import com.tperuch.voteservice.dto.response.VoteResponseDto;
import com.tperuch.voteservice.dto.response.VoteResultResponseDto;
import com.tperuch.voteservice.entity.SessionStatusEntity;
import com.tperuch.voteservice.entity.VoteEntity;
import com.tperuch.voteservice.repository.VoteRepository;
import jakarta.persistence.EntityNotFoundException;
import org.hibernate.annotations.NaturalId;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class VoteService {
    @Autowired
    private SessionStatusService sessionStatusService;
    @Autowired
    private VoteRepository voteRepository;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private SessionResultService sessionResultService;
    Logger logger = LoggerFactory.getLogger(VoteService.class);

    public VoteResponseDto saveVote(VoteDto voteDto){
        logger.info("Salvando voto - id do associado:{}", voteDto.getIdAssociate());
        checkIfSessionExists(voteDto.getIdSession());
        if(!isSessionOpen(voteDto.getIdSession())){
            logger.error("A sessão informada não esta aberta para votação, escolha outra");
            throw new EntityNotFoundException("A sessão informada não esta aberta para votação, escolha outra");
        }
        if(associateAlreadyVotedInSession(voteDto.getIdSession(), voteDto.getIdAssociate())){
            logger.error("O associado informado ja votou nessa sessão de votação");
            throw new IllegalArgumentException(
                    "O associado "+voteDto.getIdAssociate()+ " ja votou na sessao "+voteDto.getIdSession());
        }
        VoteEntity entity = buildVoteEntity(voteDto);
        logger.info("Salvando voto");
        VoteEntity savedEntity = voteRepository.save(entity);
        return modelMapper.map(savedEntity, VoteResponseDto.class);
    }

    public VoteResultResponseDto getVotesAndSessionResult(Long sessionId){
        checkIfSessionExists(sessionId);
        if(isSessionOpen(sessionId)){
            logger.error("Essa votação segue em aberto, para visualizar o resultado selecione uma já encerrada");
            throw new IllegalArgumentException("Essa votação segue em aberto, para visualizar o resultado selecione uma já encerrada");
        }
        List<VoteEntity> votes = getVotesFromSession(sessionId);
        VoteResultResponseDto result = buildResultDto(votes, sessionId);
        sessionResultService.sendResultToQueue(result);
        return result;
    }

    private boolean sessionNotExists(Long sessionId) {
        SessionStatusEntity statusEntity = sessionStatusService.getSessionBySessionId(sessionId);
        return Objects.isNull(statusEntity);
    }

    private boolean isSessionOpen(Long sessionId) {
        logger.info("Verificando status da sessão - id da sessão:{}", sessionId);
        return sessionStatusService.isSessionOpen(sessionId);
    }

    private void checkIfSessionExists(Long sessionId) {
        if(sessionNotExists(sessionId)){
            logger.error("A sessao informada nao existe");
            throw new EntityNotFoundException("A sessao informada nao existe");
        }
    }

    private List<VoteEntity> getVotesFromSession(Long sessionId) {
        logger.info("Buscando votos da sessão {}", sessionId);
        return voteRepository.findBySessionId(sessionId);
    }

    private VoteResultResponseDto buildResultDto(List<VoteEntity> votes, Long sessionId) {
        logger.info("Contando votos da sessão {} e mapeando dados de retorno", sessionId);
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
            case 1 -> "APROVADA";
            default -> "REJEITADA";
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
        logger.info("Verificando se o associado {} ja votou na sessão {}", idAssociate, idSession);
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
