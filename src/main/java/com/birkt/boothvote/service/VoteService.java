package com.birkt.boothvote.service;

import com.birkt.boothvote.DTO.StandVoteResultDto;
import com.birkt.boothvote.entity.Stand;
import com.birkt.boothvote.entity.Vote;
import com.birkt.boothvote.repository.StandRepository;
import com.birkt.boothvote.repository.VoteRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class VoteService {

    private final VoteRepository voteRepository;
    private final StandRepository standRepository;

    public VoteService(VoteRepository voteRepository, StandRepository standRepository) {
        this.voteRepository = voteRepository;
        this.standRepository = standRepository;
    }

    public Vote submitVote(Long standId, int rating, String feedback, String guestId) {
        if (guestId != null && voteRepository.existsByStandIdAndGuestId(standId, guestId)) {
            throw new RuntimeException("You have already reviewed this stand");
        }

        Stand stand = standRepository.findById(standId)
                .orElseThrow(() -> new RuntimeException("Stand not found"));

        Vote vote = new Vote();
        vote.setStandId(standId);
        vote.setConferenceId(stand.getConference().getId());
        vote.setRating(rating);
        vote.setFeedback(feedback);
        vote.setGuestId(guestId);
        vote.setTime(LocalDateTime.now());

        return voteRepository.save(vote);
    }

    public List<Vote> getVotesForConference(Long conferenceId) {
        return voteRepository.findByConferenceId(conferenceId);
    }

    public List<Vote> getVotesForStand(Long standId) {
        return voteRepository.findByStandId(standId);
    }

    public List<StandVoteResultDto> getVoteResultsForConference(Long conferenceId) {
        // hent alle stands for konferansen
        List<Stand> stands = standRepository.findByConferenceId(conferenceId);

        List<StandVoteResultDto> results = new ArrayList<>();

        for (Stand stand : stands) {
            // hent stemmer for denne standen
            List<Vote> votes = voteRepository.findByStandId(stand.getId());

            int totalVotes = votes.size();
            double avgRating = totalVotes > 0
                    ? votes.stream().mapToInt(Vote::getRating).average().orElse(0)
                    : 0;

            results.add(new StandVoteResultDto(
                    stand.getId(),
                    stand.getFirmaNavn(),
                    totalVotes,
                    avgRating
            ));
        }

        return results;
    }
}
