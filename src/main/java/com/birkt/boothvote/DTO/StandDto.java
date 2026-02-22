package com.birkt.boothvote.DTO;

public record StandDto(
        Long conferenceId,
        String firmaNavn,
        String info,
        String bildeUrl,
        String positionIdentifier
) {}