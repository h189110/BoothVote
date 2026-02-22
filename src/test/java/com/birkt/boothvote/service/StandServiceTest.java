package com.birkt.boothvote.service;

import com.birkt.boothvote.entity.Conference;
import com.birkt.boothvote.entity.Stand;
import com.birkt.boothvote.repository.ConferenceRepository;
import com.birkt.boothvote.repository.StandRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class StandServiceTest {

    private StandRepository standRepository;
    private ConferenceRepository conferenceRepository;
    private StandService standService;

    @BeforeEach
    void setUp() {
        standRepository = mock(StandRepository.class);
        conferenceRepository = mock(ConferenceRepository.class);
        standService = new StandService(standRepository, conferenceRepository);
    }

    @Test
    void createStand_success() {
        Conference conf = new Conference();

        when(conferenceRepository.findById(1L)).thenReturn(Optional.of(conf));

        Stand stand = new Stand();
        stand.setFirmaNavn("Firma A");

        when(standRepository.save(any(Stand.class))).thenReturn(stand);

        Stand result = standService.createStand(1L, "Firma A", "Info", "logo.png", "A1");

        assertNotNull(result);
        assertEquals("Firma A", result.getFirmaNavn());
    }

    @Test
    void getStandsForConference_success() {
        Stand stand = new Stand();
        stand.setFirmaNavn("Firma A");

        when(standRepository.findByConferenceId(1L)).thenReturn(List.of(stand));

        List<Stand> results = standService.getStandsForConference(1L);
        assertEquals(1, results.size());
        assertEquals("Firma A", results.get(0).getFirmaNavn());
    }

    @Test
    void updateStand_success() {
        Stand stand = new Stand();
        stand.setFirmaNavn("Old Name");

        when(standRepository.findById(1L)).thenReturn(Optional.of(stand));
        when(standRepository.save(any(Stand.class))).thenReturn(stand);

        Stand updated = standService.updateStand(1L, "New Name", null, null, null);

        assertEquals("New Name", updated.getFirmaNavn());
    }

    @Test
    void deleteStand_success() {
        Stand stand = new Stand();

        when(standRepository.findById(1L)).thenReturn(Optional.of(stand));
        doNothing().when(standRepository).delete(stand);

        assertDoesNotThrow(() -> standService.deleteStand(1L));
        verify(standRepository, times(1)).delete(stand);
    }
}