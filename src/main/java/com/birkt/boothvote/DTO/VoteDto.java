package com.birkt.boothvote.DTO;

public record VoteDto(
        Long standId,
        int rating,
        String feedback,
        String guestId
) {}