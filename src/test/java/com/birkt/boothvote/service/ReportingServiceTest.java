package com.birkt.boothvote.service;

import com.birkt.boothvote.DTO.StandVoteResultDto;
import com.birkt.boothvote.entity.Conference;
import com.birkt.boothvote.repository.ConferenceRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ReportingServiceTest {

    private ConferenceRepository conferenceRepository;
    private VoteService voteService;
    private EmailService emailService;
    private ReportingService reportingService;

    @BeforeEach
    void setUp() {
        conferenceRepository = mock(ConferenceRepository.class);
        voteService = mock(VoteService.class);
        emailService = mock(EmailService.class);
        reportingService = new ReportingService(conferenceRepository, voteService, emailService);
    }

    @Test
    void checkAndSendCompletedConferenceReports_sendsEmail() {
        Conference conference = new Conference("Test Conf", "organizer@example.com", LocalDateTime.now().minusDays(1), LocalDateTime.now().minusHours(1));
        conference.setId(1L);
        conference.setEmailSent(false);

        when(conferenceRepository.findAllByVoteEndTimeBeforeAndEmailSentFalse(any(LocalDateTime.class)))
                .thenReturn(List.of(conference));

        StandVoteResultDto result1 = new StandVoteResultDto(1L, "Stand A", 10, 4.5);
        when(voteService.getVoteResultsForConference(1L)).thenReturn(List.of(result1));

        reportingService.checkAndSendCompletedConferenceReports();

        ArgumentCaptor<String> bodyCaptor = ArgumentCaptor.forClass(String.class);
        verify(emailService).sendEmail(eq("organizer@example.com"), contains("Test Conf"), bodyCaptor.capture());
        
        String body = bodyCaptor.getValue();
        assertTrue(body.contains("Stand A"));
        assertTrue(body.contains("10"));
        assertTrue(body.contains("4.50"));

        verify(conferenceRepository).save(conference);
        assertTrue(conference.isEmailSent());
    }
}
