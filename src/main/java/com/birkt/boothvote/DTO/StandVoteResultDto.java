package com.birkt.boothvote.DTO;

public record StandVoteResultDto(
        Long standId,
        String firmaNavn,
        int totalVotes,
        double averageRating
) {}