package com.birkt.boothvote.service;


import com.birkt.boothvote.DTO.CreateConferenceRequest;
import com.birkt.boothvote.entity.Conference;
import com.birkt.boothvote.repository.ConferenceRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
@Service
public class ConferenceService {

    private final ConferenceRepository conferenceRepository;

    public ConferenceService(ConferenceRepository conferenceRepository) {
        this.conferenceRepository = conferenceRepository;
    }

    //  Opprett konferanse
    public Conference createConference(CreateConferenceRequest request) {
        Conference conference = new Conference(
                request.getName(),
                request.getOrganizerEmail(),
                request.getStartTime(),
                request.getVoteEndTime()
        );
        return conferenceRepository.save(conference);
    }

    // Hent alle konferanser
    public List<Conference> getAllConferences() {
        return conferenceRepository.findAll();
    }

    // Hent én konferanse
    public Conference getConference(Long id) {
        return conferenceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Conference not found"));
    }

    public Conference getCurrentConference() {
        LocalDateTime now = LocalDateTime.now();
        return conferenceRepository.findAll().stream()
                .filter(c -> !now.isBefore(c.getStartTime()) && !now.isAfter(c.getVoteEndTime()))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("No active conference"));
    }

    // Oppdater konferanse
    public Conference updateConference(Long id, CreateConferenceRequest request) {
        Conference conference = conferenceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Conference not found"));

        conference.setName(request.getName());
        conference.setOrganizerEmail(request.getOrganizerEmail());
        conference.setStartTime(request.getStartTime());
        conference.setVoteEndTime(request.getVoteEndTime());

        return conferenceRepository.save(conference);
    }

    // Slett konferanse
    public void deleteConference(Long id) {
        Conference conference = conferenceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Conference not found"));
        conferenceRepository.delete(conference);
    }
}