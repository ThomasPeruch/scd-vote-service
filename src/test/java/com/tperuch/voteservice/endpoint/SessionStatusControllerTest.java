package com.tperuch.voteservice.endpoint;

import com.tperuch.voteservice.dto.response.SessionStatusResponseDto;
import com.tperuch.voteservice.service.SessionStatusService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;

import static com.tperuch.voteservice.stub.sessionstatus.SessionStatusResponseDtoStub.getSessionStatusResponseDtoStub;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(SessionStatusController.class)
@AutoConfigureMockMvc
class SessionStatusControllerTest {
    @Autowired
    private MockMvc mvc;
    @MockBean
    private SessionStatusService sessionStatusService;

    @Test
    void getAllSessions()throws Exception{
        SessionStatusResponseDto statusResponseDto = getSessionStatusResponseDtoStub();
        when(sessionStatusService.getAllSessions()).thenReturn(List.of(statusResponseDto));

        MvcResult result = mvc.perform(MockMvcRequestBuilders.get("http://localhost:8081/session-status")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        String content = result.getResponse().getContentAsString();

        assertTrue(content.contains("\"sessionStatus\":\""+statusResponseDto.getSessionStatus()+"\""));
    }
}