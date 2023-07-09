package com.tperuch.voteservice.endpoint;

import com.tperuch.voteservice.dto.response.SessionStatusResponseDto;
import com.tperuch.voteservice.service.SessionStatusService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/session-status")
@Tag(name = "Status da sessão", description = "Operações relacionadas a status de sessão")
public class SessionStatusController {
    @Autowired
    public SessionStatusService statusService;

    @Operation(summary = "Busca os status das sessões")
    @GetMapping
    public ResponseEntity<List<SessionStatusResponseDto>> getAllSessionsStatus() {
        return new ResponseEntity<>(statusService.getAllSessions(), HttpStatus.OK);
    }
}
