package com.tperuch.voteservice.service;

import com.tperuch.voteservice.dto.VoteDto;
import com.tperuch.voteservice.entity.VoteEntity;
import com.tperuch.voteservice.repository.VoteRepository;
import jakarta.persistence.EntityNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class VoteService {
    @Autowired
    private SessionStatusService sessionStatusService;
    @Autowired
    private VoteRepository voteRepository;
    @Autowired
    private ModelMapper modelMapper;

    public VoteDto saveVote(VoteDto voteDto){
        if(!sessionStatusService.isSessionOpen(voteDto.getIdSession())){
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
