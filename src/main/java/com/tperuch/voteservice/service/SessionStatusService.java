package com.tperuch.voteservice.service;

import com.google.gson.Gson;
import com.tperuch.voteservice.dto.SessionStatusDto;
import com.tperuch.voteservice.dto.response.SessionStatusResponseDto;
import com.tperuch.voteservice.entity.SessionStatusEntity;
import com.tperuch.voteservice.repository.SessionStatusRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SessionStatusService {
    @Autowired
    private SessionStatusRepository statusRepository;
    @Autowired
    private Gson gson;
    Logger logger = LoggerFactory.getLogger(SessionStatusService.class);

    public boolean isSessionOpen(Long id){
        return statusRepository.existsBySessionIdAndSessionStatus(id, "OPEN");
    }

    public List<SessionStatusResponseDto> getAllSessions(){
        List<SessionStatusEntity> entities = statusRepository.findAll();
        return entities.stream().map(this::mapToSessionStatusResponseDto).toList();
    }

    public void refreshSessionStatus(Message message){
        byte[] messageConverted = convertMessageToBytes(message);
        String json = convertBytesToJson(messageConverted);
        logger.info("Convertendo json da mensagem para objeto");
        SessionStatusDto sessionStatus = convertJsonToDto(json);
        SessionStatusEntity entity = convertDtoToEntity(sessionStatus);
        updateEntity(entity);
    }

    public SessionStatusEntity getSessionBySessionId(Long sessionId) {
        return statusRepository.findBySessionId(sessionId);
    }

    private SessionStatusResponseDto mapToSessionStatusResponseDto(SessionStatusEntity entity) {
        SessionStatusResponseDto dto = new SessionStatusResponseDto();
        dto.setIdSession(entity.getSessionId());
        dto.setSessionStatus(entity.getSessionStatus());
        dto.setId(entity.getId());
        return dto;
    }

    private void updateEntity(SessionStatusEntity entity) {
        SessionStatusEntity statusEntity = getSessionBySessionId(entity.getSessionId());
        statusEntity.setSessionStatus(entity.getSessionStatus());
        logger.info("Salvando status da sessão na base de dados");
        statusRepository.save(statusEntity);
    }

    private SessionStatusEntity convertDtoToEntity(SessionStatusDto sessionStatus) {
        SessionStatusEntity entity = new SessionStatusEntity();
        entity.setSessionStatus(sessionStatus.getStatus());
        entity.setSessionId(sessionStatus.getSessionId());
        return entity;
    }

    private SessionStatusDto convertJsonToDto(String json) {
        return gson.fromJson(json, SessionStatusDto.class);
    }

    private String convertBytesToJson(byte[] messageConverted) {
        return new String(messageConverted);
    }

    private byte[] convertMessageToBytes(Message message) {
        return message.getBody();
    }
}
