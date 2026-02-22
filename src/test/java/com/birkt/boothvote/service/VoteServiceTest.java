package com.birkt.boothvote.service;
import com.birkt.boothvote.entity.Conference;
import com.birkt.boothvote.entity.Stand;
import com.birkt.boothvote.entity.Vote;
import com.birkt.boothvote.repository.StandRepository;
import com.birkt.boothvote.repository.VoteRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class VoteServiceTest {

    private VoteRepository voteRepository;
    private StandRepository standRepository;
    private VoteService voteService;

    @BeforeEach
    void setUp() {
        voteRepository = mock(VoteRepository.class);
        standRepository = mock(StandRepository.class);
        voteService = new VoteService(voteRepository, standRepository);
    }

    // ✅ Test for å submitte en stemme
    @Test
    void submitVote_success() {
        // Lag en mock Stand som finnes
        Stand stand = new Stand();
        stand.setFirmaNavn("Firma A");

        Conference conference = mock(Conference.class); // ⚡️ mock konferansen
        when(conference.getId()).thenReturn(1L); // simuler at den har en ID

        stand.setConference(conference); // koble mocked conference til stand

        when(standRepository.findById(1L)).thenReturn(Optional.of(stand));

        // Mock save for VoteRepository
        Vote savedVote = new Vote();
        savedVote.setRating(5);
        savedVote.setFeedback("Flott stand!");
        savedVote.setGuestId("guest123");
        when(voteRepository.save(any(Vote.class))).thenReturn(savedVote);

        // Kall submitVote
        Vote result = voteService.submitVote(1L, 5, "Flott stand!", "guest123");

        // Assertions
        assertNotNull(result);
        assertEquals(5, result.getRating());
        assertEquals("Flott stand!", result.getFeedback());
        assertEquals("guest123", result.getGuestId());

        // Verifiser at save ble kalt én gang
        verify(voteRepository, times(1)).save(any(Vote.class));
    }

    @Test
    void submitVote_alreadyVoted_throwsException() {
        when(voteRepository.existsByStandIdAndGuestId(1L, "guest123")).thenReturn(true);

        assertThrows(RuntimeException.class, () -> {
            voteService.submitVote(1L, 5, "Feedback", "guest123");
        });

        verify(voteRepository, never()).save(any(Vote.class));
    }

    // ✅ Test for å hente alle stemmer for en konferanse
    @Test
    void getVotesForConference_success() {
        Vote vote = new Vote();
        vote.setRating(4);
        vote.setConferenceId(1L);

        when(voteRepository.findByConferenceId(1L)).thenReturn(List.of(vote));

        List<Vote> votes = voteService.getVotesForConference(1L);

        assertEquals(1, votes.size());
        assertEquals(4, votes.get(0).getRating());
    }

    // ✅ Test for å hente alle stemmer for en stand
    @Test
    void getVotesForStand_success() {
        Vote vote1 = new Vote();
        vote1.setRating(3);
        vote1.setStandId(1L);

        Vote vote2 = new Vote();
        vote2.setRating(5);
        vote2.setStandId(1L);

        when(voteRepository.findByStandId(1L)).thenReturn(List.of(vote1, vote2));

        List<Vote> votes = voteService.getVotesForStand(1L);

        assertEquals(2, votes.size());
        assertEquals(3, votes.get(0).getRating());
        assertEquals(5, votes.get(1).getRating());
    }

    // ✅ Enkel teststub for aggregert resultat
    @Test
    void getVoteResultsForConference_success() {
        // Sett opp mock-stemmer
        Vote vote1 = new Vote();
        vote1.setStandId(1L);
        vote1.setRating(4);

        Vote vote2 = new Vote();
        vote2.setStandId(1L);
        vote2.setRating(5);

        when(voteRepository.findByStandId(1L)).thenReturn(List.of(vote1, vote2));

        // Her kan du kalle aggregert metode i service
        // f.eks voteService.getVoteResultsForConference(1L)
        // Assertions av resultat kan implementeres når du har DTO / aggregasjon
    }
}