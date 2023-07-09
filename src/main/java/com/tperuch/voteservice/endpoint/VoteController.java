package com.tperuch.voteservice.endpoint;

import com.tperuch.voteservice.dto.VoteDto;
import com.tperuch.voteservice.dto.response.VoteResultResponseDto;
import com.tperuch.voteservice.service.VoteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/vote")
@Tag(name = "Votos", description = "Operações relacionadas a votos")
public class VoteController {
    @Autowired
    public VoteService voteService;

    @Operation(summary = "Registra voto em uma sessão")
    @PostMapping
    public ResponseEntity<VoteDto> vote(@Valid @RequestBody VoteDto voteDto) {
        return new ResponseEntity<>(voteService.saveVote(voteDto), HttpStatus.CREATED);
    }

    @Operation(summary = "Conta votos de uma sessão e mostra o resultado")
    @GetMapping(value = "/{sessionId}")
    public ResponseEntity<VoteResultResponseDto> getVotesAndSessionResult(@PathVariable @NotNull Long sessionId) {
        return new ResponseEntity<>(voteService.getVotesAndSessionResult(sessionId), HttpStatus.OK);
    }
}
