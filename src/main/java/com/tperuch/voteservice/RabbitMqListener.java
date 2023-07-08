package com.tperuch.voteservice;

import com.tperuch.voteservice.service.SessionStatusService;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
public class RabbitMqListener {

    @Autowired
    private SessionStatusService statusService;

    @RabbitListener(queues = {"session-status-queue"})
    public void receiveSessionStatus(@Payload Message message) {
        statusService.refreshSessionStatus(message);
    }
}
