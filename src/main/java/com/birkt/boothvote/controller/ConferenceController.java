package com.birkt.boothvote.controller;

import com.birkt.boothvote.DTO.CreateConferenceRequest;
import com.birkt.boothvote.entity.Conference;
import com.birkt.boothvote.service.ConferenceService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/conferences")
public class ConferenceController {

    private final ConferenceService conferenceService;

    public ConferenceController(ConferenceService conferenceService) {
        this.conferenceService = conferenceService;
    }

    @PostMapping
    public Conference createConference(
            @Valid @RequestBody CreateConferenceRequest request) {
        return conferenceService.createConference(request);
    }

    @GetMapping
    public List<Conference> getAllConferences() {
        return conferenceService.getAllConferences();
    }

    @GetMapping("/{id}")
    public Conference getConference(@PathVariable Long id) {
        return conferenceService.getConference(id);
    }

    @PutMapping("/{id}")
    public Conference updateConference(@PathVariable Long id, @RequestBody CreateConferenceRequest request) {
        return conferenceService.updateConference(id, request);
    }

    @DeleteMapping("/{id}")
    public void deleteConference(@PathVariable Long id) {
        conferenceService.deleteConference(id);
    }
}

