package com.tperuch.voteservice.service;

import com.google.gson.Gson;
import com.tperuch.voteservice.dto.SessionStatusDto;
import com.tperuch.voteservice.entity.SessionStatusEntity;
import com.tperuch.voteservice.repository.SessionStatusRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.amqp.core.Message;

import java.util.List;

import static com.tperuch.voteservice.stub.message.MessageStub.getMessageStub;
import static com.tperuch.voteservice.stub.sessionstatus.SessionStatusDtoStub.getSessionStatusDto;
import static com.tperuch.voteservice.stub.sessionstatus.SessionStatusEntityStub.getSessionStatusStub;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SessionStatusServiceTest {
    @Mock
    private SessionStatusRepository statusRepository;
    @Mock
    private Gson gson;
    @InjectMocks
    private SessionStatusService sessionStatusService;

    @Test
    public void isSessionOpen() {
        when(statusRepository.existsBySessionIdAndSessionStatus(1L, "OPEN")).thenReturn(true);
        assertTrue(sessionStatusService.isSessionOpen(1L));
    }

    @Test
    public void isSessionClosed() {
        when(statusRepository.existsBySessionIdAndSessionStatus(1L, "OPEN")).thenReturn(false);
        assertFalse(sessionStatusService.isSessionOpen(1L));
    }

    @Test
    public void getAllSessions() {
        List<SessionStatusEntity> status = List.of(getSessionStatusStub());
        when(statusRepository.findAll()).thenReturn(status);
        sessionStatusService.getAllSessions();
        verify(statusRepository, times(1)).findAll();
    }

    @Test
    public void shouldRefreshSessionStatus() {
        Message message = getMessageStub();
        SessionStatusDto sessionStatusDto = getSessionStatusDto();
        Gson _gson = new Gson();
        String json = _gson.toJson(sessionStatusDto);
        SessionStatusEntity statusEntity = getSessionStatusStub();
        statusEntity.setSessionStatus("CLOSED");

        when(gson.fromJson(json, SessionStatusDto.class)).thenReturn(sessionStatusDto);
        when(statusRepository.findBySessionId(1L)).thenReturn(statusEntity);
        when(statusRepository.save(statusEntity)).thenReturn(getSessionStatusStub());

        sessionStatusService.refreshSessionStatus(message);
    }
}