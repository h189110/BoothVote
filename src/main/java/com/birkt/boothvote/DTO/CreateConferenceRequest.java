package com.birkt.boothvote.DTO;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

public class CreateConferenceRequest {

    @NotBlank
    private String name;

    @NotBlank
    private String organizerEmail;

    @NotNull
    private LocalDateTime startTime;

    @NotNull
    private LocalDateTime voteEndTime;

    public String getName() { return name; }
    public String getOrganizerEmail() { return organizerEmail; }
    public LocalDateTime getStartTime() { return startTime; }
    public LocalDateTime getVoteEndTime() { return voteEndTime; }

    public void setName(String name) { this.name = name; }
    public void setOrganizerEmail(String organizerEmail) { this.organizerEmail = organizerEmail; }
    public void setStartTime(LocalDateTime startTime) { this.startTime = startTime; }
    public void setVoteEndTime(LocalDateTime voteEndTime) { this.voteEndTime = voteEndTime; }
}