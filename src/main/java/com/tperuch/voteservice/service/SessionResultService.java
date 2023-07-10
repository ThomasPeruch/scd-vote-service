package com.tperuch.voteservice.service;

import com.google.gson.Gson;
import com.tperuch.voteservice.dto.response.VoteResultResponseDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class SessionResultService {

    @Value("${spring.rabbitmq.exchange}")
    private String exchange;
    @Value("${spring.rabbitmq.session-result.queue}")
    private String sessionResultQueue;
    @Autowired
    private RabbitTemplate rabbitTemplate;
    Logger logger = LoggerFactory.getLogger(SessionResultService.class);

    @Autowired
    private Gson gson;

    public void sendResultToQueue(VoteResultResponseDto voteResultResponseDto){
        logger.info("Enviando mensagem! EXCHANGE: {} - QUEUE/FILA: {}", exchange, sessionResultQueue);
        rabbitTemplate.convertAndSend(exchange, sessionResultQueue, convertObjToMessage(voteResultResponseDto));
        logger.info("Mesagem enviada! EXCHANGE: {} - QUEUE/FILA: {}", exchange, sessionResultQueue);
    }

    private Message convertObjToMessage(VoteResultResponseDto dto) {
        logger.info("Transformando dados em Json para envio de mensagem");
        String json = convertObjToJson(dto);
        return new Message(json.getBytes(), new MessageProperties());
    }

    private String convertObjToJson(VoteResultResponseDto dto) {
        return gson.toJson(dto);
    }
}
