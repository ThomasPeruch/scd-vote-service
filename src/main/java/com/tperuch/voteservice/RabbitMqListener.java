package com.tperuch.voteservice;

import com.tperuch.voteservice.service.SessionStatusService;
import com.tperuch.voteservice.service.VoteService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
public class RabbitMqListener {

    @Autowired
    private SessionStatusService statusService;
    Logger logger = LoggerFactory.getLogger(RabbitMqListener.class);

    @RabbitListener(queues = {"session-status-queue"})
    public void receiveSessionStatus(@Payload Message message) {
        logger.info("Recebendo mensagem com status da sessão");
        statusService.refreshSessionStatus(message);
    }
}
