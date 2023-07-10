package com.tperuch.voteservice.stub.message;

import com.google.gson.Gson;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;

import static com.tperuch.voteservice.stub.sessionstatus.SessionStatusDtoStub.getSessionStatusDto;

public class MessageStub {
    public static Message getMessageStub(){
        Gson gson = new Gson();
        String json = gson.toJson(getSessionStatusDto());
        return new Message(json.getBytes(), new MessageProperties());
    }
}
