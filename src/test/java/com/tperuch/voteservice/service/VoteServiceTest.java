package com.tperuch.voteservice.service;

import com.tperuch.voteservice.dto.VoteDto;
import com.tperuch.voteservice.dto.response.VoteResponseDto;
import com.tperuch.voteservice.dto.response.VoteResultResponseDto;
import com.tperuch.voteservice.entity.SessionStatusEntity;
import com.tperuch.voteservice.entity.VoteEntity;
import com.tperuch.voteservice.repository.VoteRepository;
import com.tperuch.voteservice.stub.vote.VoteEntityStub;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.util.List;

import static com.tperuch.voteservice.stub.sessionstatus.SessionStatusEntityStub.getSessionStatusStub;
import static com.tperuch.voteservice.stub.vote.VoteDtoStub.getVoteDtoStub;
import static com.tperuch.voteservice.stub.vote.VoteEntityStub.getVoteEntityStub;
import static com.tperuch.voteservice.stub.vote.VoteResponseDtoStub.getVoteResponseDtoStub;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class VoteServiceTest {

    @Mock
    private SessionStatusService sessionStatusService;
    @Mock
    private VoteRepository voteRepository;
    @Mock
    private ModelMapper modelMapper;
    @InjectMocks
    private VoteService voteService;


    @Test
    void shouldSaveVote() {
        VoteDto voteDto = getVoteDtoStub();
        SessionStatusEntity statusEntity = getSessionStatusStub();
        VoteEntity voteEntity = getVoteEntityStub();
        VoteResponseDto responseDto = getVoteResponseDtoStub();

        when(sessionStatusService.getSessionBySessionId(1L)).thenReturn(statusEntity);
        when(sessionStatusService.isSessionOpen(1L)).thenReturn(true);
        when(voteRepository.existsBySessionIdAndAssociateId(1L, 1L)).thenReturn(false);
        when(voteRepository.save(any())).thenReturn(voteEntity);
        when(modelMapper.map(voteEntity, VoteResponseDto.class)).thenReturn(responseDto);

        voteService.saveVote(voteDto);

        verify(voteRepository, times(1)).save(any(VoteEntity.class));
    }

    @Test
    void shouldNotSaveVoteWhenSessionDontExists() {
        VoteDto voteDto = getVoteDtoStub();

        when(sessionStatusService.getSessionBySessionId(1L)).thenReturn(null);

        Exception exception = assertThrows(Exception.class, () -> voteService.saveVote(voteDto));

        assertEquals("A sessao informada nao existe", exception.getMessage());
        assertEquals(exception.getClass().getName(), EntityNotFoundException.class.getName());
    }

    @Test
    void shouldNotSaveVoteWhenSessionIsClosed() {
        VoteDto voteDto = getVoteDtoStub();
        SessionStatusEntity statusEntity = getSessionStatusStub();

        when(sessionStatusService.getSessionBySessionId(1L)).thenReturn(statusEntity);
        when(sessionStatusService.isSessionOpen(1L)).thenReturn(false);

        Exception exception = assertThrows(Exception.class, () -> voteService.saveVote(voteDto));

        assertEquals("A sessão informada não esta aberta para votação, escolha outra", exception.getMessage());
        assertEquals(exception.getClass().getName(), EntityNotFoundException.class.getName());
    }

    @Test
    void shouldNotSaveVoteWhenAssociateAlreadyVotedInSession() {
        VoteDto voteDto = getVoteDtoStub();
        SessionStatusEntity statusEntity = getSessionStatusStub();

        when(sessionStatusService.getSessionBySessionId(1L)).thenReturn(statusEntity);
        when(sessionStatusService.isSessionOpen(1L)).thenReturn(true);
        when(voteRepository.existsBySessionIdAndAssociateId(1L,1L)).thenReturn(true);

        Exception exception = assertThrows(Exception.class, () -> voteService.saveVote(voteDto));

        assertEquals("O associado "+voteDto.getIdAssociate()+" ja votou na sessao "+voteDto.getIdSession(),exception.getMessage());
        assertEquals(exception.getClass().getName(), IllegalArgumentException.class.getName());
    }

    @Test
    void getVotesAndSessionResultApproved() {
        List<VoteEntity> votes = List.of(getVoteEntityStub());
        SessionStatusEntity statusEntity = getSessionStatusStub();
        when(sessionStatusService.getSessionBySessionId(1L)).thenReturn(statusEntity);
        when(sessionStatusService.isSessionOpen(1L)).thenReturn(false);
        when(voteRepository.findBySessionId(1L)).thenReturn(votes);

        VoteResultResponseDto result = voteService.getVotesAndSessionResult(1L);

        assertEquals("APROVADA", result.getResult());
    }

    @Test
    void getVotesAndSessionResultDraw() {
        VoteEntity positiveVote = getVoteEntityStub();
        VoteEntity negativeVote = getVoteEntityStub();
        negativeVote.setVote(false);
        List<VoteEntity> votes = List.of(positiveVote, negativeVote);
        SessionStatusEntity statusEntity = getSessionStatusStub();
        when(sessionStatusService.getSessionBySessionId(1L)).thenReturn(statusEntity);
        when(sessionStatusService.isSessionOpen(1L)).thenReturn(false);
        when(voteRepository.findBySessionId(1L)).thenReturn(votes);

        VoteResultResponseDto result = voteService.getVotesAndSessionResult(1L);

        assertEquals("EMPATE", result.getResult());
    }

    @Test
    void getVotesAndSessionResultRejected() {
        VoteEntity negativeVote = getVoteEntityStub();
        negativeVote.setVote(false);
        List<VoteEntity> votes = List.of(negativeVote);
        SessionStatusEntity statusEntity = getSessionStatusStub();
        when(sessionStatusService.getSessionBySessionId(1L)).thenReturn(statusEntity);
        when(sessionStatusService.isSessionOpen(1L)).thenReturn(false);
        when(voteRepository.findBySessionId(1L)).thenReturn(votes);

        VoteResultResponseDto result = voteService.getVotesAndSessionResult(1L);

        assertEquals("REJEITADA", result.getResult());
    }

    @Test
    void cannotGetVotesAndSessionResultWhenSessionIsOpen() {
        SessionStatusEntity statusEntity = getSessionStatusStub();
        when(sessionStatusService.getSessionBySessionId(1L)).thenReturn(statusEntity);
        when(sessionStatusService.isSessionOpen(1L)).thenReturn(true);

        Exception exception = assertThrows(Exception.class, () -> voteService.getVotesAndSessionResult(1L));

        assertEquals("Essa votação segue em aberto, para visualizar o resultado selecione uma já encerrada",exception.getMessage());
        assertEquals(exception.getClass().getName(), IllegalArgumentException.class.getName());
    }
}