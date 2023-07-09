package com.tperuch.voteservice.endpoint;

import com.tperuch.voteservice.dto.VoteDto;
import com.tperuch.voteservice.service.VoteService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/vote")
public class VoteController {
    @Autowired
    public VoteService voteService;

    @PostMapping
    public ResponseEntity<VoteDto> vote(@Valid @RequestBody VoteDto voteDto){
        return new ResponseEntity<>(voteService.saveVote(voteDto), HttpStatus.CREATED);
    }
}
