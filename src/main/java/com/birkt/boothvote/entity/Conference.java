package com.birkt.boothvote.entity;

import jakarta.persistence.Entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "conferences")
public class Conference {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String organizerEmail;

    private LocalDateTime startTime;

    private LocalDateTime voteEndTime;

    private boolean emailSent = false;

    public Conference() {
    }

    public Conference(String name, String organizerEmail, LocalDateTime startTime, LocalDateTime voteEndTime) {
        this.name = name;
        this.organizerEmail = organizerEmail;
        this.startTime = startTime;
        this.voteEndTime = voteEndTime;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public String getOrganizerEmail() { return organizerEmail; }
    public LocalDateTime getStartTime() { return startTime; }
    public LocalDateTime getVoteEndTime() { return voteEndTime; }

    public void setName(String name) { this.name = name; }
    public void setOrganizerEmail(String organizerEmail) { this.organizerEmail = organizerEmail; }
    public void setStartTime(LocalDateTime startTime) { this.startTime = startTime; }
    public void setVoteEndTime(LocalDateTime voteEndTime) { this.voteEndTime = voteEndTime; }

    public boolean isEmailSent() { return emailSent; }
    public void setEmailSent(boolean emailSent) { this.emailSent = emailSent; }


}