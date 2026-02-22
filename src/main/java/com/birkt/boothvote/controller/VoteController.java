package com.birkt.boothvote.controller;

import com.birkt.boothvote.DTO.VoteDto;
import com.birkt.boothvote.entity.Vote;
import com.birkt.boothvote.service.VoteService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/votes")
public class VoteController {

    private final VoteService voteService;

    public VoteController(VoteService voteService) {
        this.voteService = voteService;
    }

    @PostMapping
    public ResponseEntity<Vote> submitVote(@RequestBody VoteDto dto) {
        Vote vote = voteService.submitVote(dto.standId(), dto.rating(), dto.feedback(), dto.guestId());
        return ResponseEntity.ok(vote);
    }

    @GetMapping("/conference/{conferenceId}")
    public ResponseEntity<List<Vote>> getVotesForConference(@PathVariable Long conferenceId) {
        List<Vote> votes = voteService.getVotesForConference(conferenceId);
        return ResponseEntity.ok(votes);
    }
}