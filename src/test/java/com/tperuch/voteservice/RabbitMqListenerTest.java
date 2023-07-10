package com.tperuch.voteservice;

import com.tperuch.voteservice.service.SessionStatusService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.tperuch.voteservice.stub.message.MessageStub.getMessageStub;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RabbitMqListenerTest {

    @Mock
    private SessionStatusService sessionStatusService;
    @InjectMocks
    private RabbitMqListener rabbitMqListener;

    @Test
    void shouldReceiveSessionStatus() {
        doNothing().when(sessionStatusService).refreshSessionStatus(any());
        rabbitMqListener.receiveSessionStatus(getMessageStub());
        verify(sessionStatusService, times(1)).refreshSessionStatus(any());
    }

}