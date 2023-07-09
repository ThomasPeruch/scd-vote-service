package com.tperuch.voteservice.endpoint;

import com.tperuch.voteservice.dto.VoteDto;
import com.tperuch.voteservice.dto.response.VoteResultResponseDto;
import com.tperuch.voteservice.service.VoteService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/vote")
public class VoteController {
    @Autowired
    public VoteService voteService;

    @PostMapping
    public ResponseEntity<VoteDto> vote(@Valid @RequestBody VoteDto voteDto) {
        return new ResponseEntity<>(voteService.saveVote(voteDto), HttpStatus.CREATED);
    }

    @GetMapping(value = "/{sessionId}")
    public ResponseEntity<VoteResultResponseDto> getVotesAndSessionResult(@PathVariable @NotNull Long sessionId) {
        return new ResponseEntity<>(voteService.getVotesAndSessionResult(sessionId), HttpStatus.OK);
    }
}
