package com.tperuch.voteservice.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "session_status")
public class SessionStatusEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "id_session")
    private Long sessionId;
    @Column(name = "session_status")
    private boolean sessionStatus;
}
