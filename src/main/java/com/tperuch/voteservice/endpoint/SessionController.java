package com.tperuch.voteservice.endpoint;

import com.tperuch.voteservice.dto.response.SessionStatusResponseDto;
import com.tperuch.voteservice.service.SessionStatusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/session-status")
public class SessionController {
    @Autowired
    public SessionStatusService statusService;

    @GetMapping
    public ResponseEntity<List<SessionStatusResponseDto>> getAllSessionsStatus() {
        return new ResponseEntity<>(statusService.getAllSessions(), HttpStatus.OK);
    }
}
