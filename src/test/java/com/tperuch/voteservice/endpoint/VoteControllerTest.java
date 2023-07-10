package com.tperuch.voteservice.endpoint;

import com.google.gson.Gson;
import com.tperuch.voteservice.dto.VoteDto;
import com.tperuch.voteservice.dto.response.VoteResponseDto;
import com.tperuch.voteservice.dto.response.VoteResultResponseDto;
import com.tperuch.voteservice.service.VoteService;
import jakarta.persistence.EntityNotFoundException;
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

import static com.tperuch.voteservice.stub.vote.VoteDtoStub.getVoteDtoStub;
import static com.tperuch.voteservice.stub.vote.VoteResponseDtoStub.getVoteResponseDtoStub;
import static com.tperuch.voteservice.stub.vote.VoteResultResponseDtoStub.getVoteResultResponseDtoStub;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(VoteController.class)
@AutoConfigureMockMvc
class VoteControllerTest {
    @Autowired
    private MockMvc mvc;
    @MockBean
    private VoteService voteService;

    @Test
    void vote() throws Exception {
        VoteResponseDto responseDto = getVoteResponseDtoStub();
        VoteDto voteDto = getVoteDtoStub();

        Gson gson = new Gson();
        String json = gson.toJson(voteDto);

        when(voteService.saveVote(any())).thenReturn(responseDto);

        MvcResult result = mvc.perform(MockMvcRequestBuilders.post("http://localhost:8081/vote")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(json))
                .andDo(print())
                .andExpect(status().isCreated())
                .andReturn();

        String content = result.getResponse().getContentAsString();
        assertTrue(content.contains("\"id\":" + responseDto.getId()));
    }

    @Test
    void getVotesAndSessionResult() throws Exception {
        VoteResultResponseDto resultResponseDto = getVoteResultResponseDtoStub();

        when(voteService.getVotesAndSessionResult(any())).thenReturn(resultResponseDto);

        MvcResult result = mvc.perform(MockMvcRequestBuilders.get("http://localhost:8081/vote/result/1")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        String content = result.getResponse().getContentAsString();
        assertTrue(content.contains("\"result\":\"" + resultResponseDto.getResult() + "\""));
    }

    @Test
    void shouldNotVoteWhenSessionDontExists() throws Exception {
        VoteDto voteDto = getVoteDtoStub();
        Gson gson = new Gson();
        String json = gson.toJson(voteDto);

        when(voteService.saveVote(any())).thenThrow(new EntityNotFoundException("A sessao informada nao existe"));

        MvcResult result = mvc.perform(MockMvcRequestBuilders.post("http://localhost:8081/vote")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(exception -> assertTrue(exception.getResolvedException() instanceof EntityNotFoundException))
                .andReturn();

        String content = result.getResponse().getContentAsString();
        assertTrue(content.contains("A sessao informada nao existe"));
    }

    @Test
    void shouldNotVoteWhenAssociateAlreadyVoted() throws Exception {
        VoteDto voteDto = getVoteDtoStub();
        Gson gson = new Gson();
        String json = gson.toJson(voteDto);
        String exceptionMessage = "O associado "+voteDto.getIdAssociate()+ " ja votou na sessao "+voteDto.getIdSession();

        when(voteService.saveVote(any())).thenThrow(new IllegalArgumentException(exceptionMessage));

        MvcResult result = mvc.perform(MockMvcRequestBuilders.post("http://localhost:8081/vote")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(exception -> assertTrue(exception.getResolvedException() instanceof IllegalArgumentException))
                .andReturn();

        String content = result.getResponse().getContentAsString();
        assertTrue(content.contains(exceptionMessage));
    }
}