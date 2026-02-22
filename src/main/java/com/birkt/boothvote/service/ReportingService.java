package com.birkt.boothvote.service;

import com.birkt.boothvote.DTO.StandVoteResultDto;
import com.birkt.boothvote.entity.Conference;
import com.birkt.boothvote.repository.ConferenceRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;

@Service
public class ReportingService {

    private final ConferenceRepository conferenceRepository;
    private final VoteService voteService;
    private final EmailService emailService;

    public ReportingService(ConferenceRepository conferenceRepository, VoteService voteService, EmailService emailService) {
        this.conferenceRepository = conferenceRepository;
        this.voteService = voteService;
        this.emailService = emailService;
    }

    @Scheduled(fixedRate = 60000) // check every minute
    @Transactional
    public void checkAndSendCompletedConferenceReports() {
        LocalDateTime now = LocalDateTime.now();
        List<Conference> completedConferences = conferenceRepository.findAllByVoteEndTimeBeforeAndEmailSentFalse(now);

        for (Conference conference : completedConferences) {
            sendReportForConference(conference);
        }
    }

    private void sendReportForConference(Conference conference) {
        List<StandVoteResultDto> results = voteService.getVoteResultsForConference(conference.getId());

        StringBuilder body = new StringBuilder();
        body.append("Conference Report for: ").append(conference.getName()).append("\n\n");
        body.append("Results per stand:\n");

        for (StandVoteResultDto result : results) {
            body.append("- ").append(result.firmaNavn())
                    .append(": Total Votes: ").append(result.totalVotes())
                    .append(", Average Rating: ").append(String.format(Locale.US, "%.2f", result.averageRating()))
                    .append("\n");
        }

        try {
            emailService.sendEmail(conference.getOrganizerEmail(), "Conference Results: " + conference.getName(), body.toString());
            conference.setEmailSent(true);
            conferenceRepository.save(conference);
        } catch (Exception e) {
            // Log error
            System.err.println("Failed to send email to " + conference.getOrganizerEmail() + ": " + e.getMessage());
        }
    }
}
