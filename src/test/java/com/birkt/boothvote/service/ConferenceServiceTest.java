package com.birkt.boothvote.service;

import com.birkt.boothvote.entity.Conference;
import com.birkt.boothvote.repository.ConferenceRepository;
import com.birkt.boothvote.DTO.CreateConferenceRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class ConferenceServiceTest {

    private ConferenceRepository conferenceRepository;
    private ConferenceService conferenceService;

    @BeforeEach
    void setUp() {
        conferenceRepository = mock(ConferenceRepository.class);
        conferenceService = new ConferenceService(conferenceRepository);
    }

    @Test
    void createConference_success() {
        CreateConferenceRequest request = new CreateConferenceRequest();
        request.setName("TechConf 2026");
        request.setStartTime(LocalDateTime.of(2026, 2, 21, 9, 0));
        request.setVoteEndTime(LocalDateTime.of(2026, 2, 21, 17, 0));
        request.setOrganizerEmail("organizer@example.com");

        Conference savedConference = new Conference(
                request.getName(),
                request.getOrganizerEmail(),
                request.getStartTime(),
                request.getVoteEndTime()
        );

        when(conferenceRepository.save(any(Conference.class))).thenReturn(savedConference);

        Conference result = conferenceService.createConference(request);

        assertNotNull(result);
        assertEquals("TechConf 2026", result.getName());
        assertEquals(LocalDateTime.of(2026, 2, 21, 9, 0), result.getStartTime());
        assertEquals(LocalDateTime.of(2026, 2, 21, 17, 0), result.getVoteEndTime());
    }

    @Test
    void getAllConferences_success() {
        Conference conf1 = new Conference("Conf 1", "org1@example.com", LocalDateTime.now(), LocalDateTime.now().plusHours(1));
        Conference conf2 = new Conference("Conf 2", "org2@example.com", LocalDateTime.now(), LocalDateTime.now().plusHours(2));

        when(conferenceRepository.findAll()).thenReturn(List.of(conf1, conf2));

        List<Conference> result = conferenceService.getAllConferences();

        assertEquals(2, result.size());
        assertEquals("Conf 1", result.get(0).getName());
        assertEquals("Conf 2", result.get(1).getName());
    }

    @Test
    void getConference_success() {
        Conference conf = new Conference("TechConf 2026", "organizer@example.com", LocalDateTime.now(), LocalDateTime.now().plusHours(8));

        when(conferenceRepository.findById(1L)).thenReturn(Optional.of(conf));

        Conference result = conferenceService.getConference(1L);

        assertNotNull(result);
        assertEquals("TechConf 2026", result.getName());
    }

    @Test
    void updateConference_success() {
        Conference conf = new Conference("Old Name", "old@example.com", LocalDateTime.now(), LocalDateTime.now().plusHours(1));
        CreateConferenceRequest request = new CreateConferenceRequest();
        request.setName("New Name");
        request.setOrganizerEmail("new@example.com");
        request.setStartTime(LocalDateTime.now());
        request.setVoteEndTime(LocalDateTime.now().plusHours(2));
        when(conferenceRepository.findById(1L)).thenReturn(Optional.of(conf));
        when(conferenceRepository.save(any(Conference.class))).thenReturn(conf);

        Conference result = conferenceService.updateConference(1L, request);

        assertEquals("New Name", result.getName());
        assertEquals("new@example.com", result.getOrganizerEmail());
        assertEquals(request.getVoteEndTime(), result.getVoteEndTime());
    }

    @Test
    void deleteConference_success() {
        Conference conf = new Conference("Conf to delete", "org@example.com", LocalDateTime.now(), LocalDateTime.now().plusHours(1));

        when(conferenceRepository.findById(1L)).thenReturn(Optional.of(conf));
        doNothing().when(conferenceRepository).delete(conf);

        assertDoesNotThrow(() -> conferenceService.deleteConference(1L));
        verify(conferenceRepository, times(1)).delete(conf);
    }
}