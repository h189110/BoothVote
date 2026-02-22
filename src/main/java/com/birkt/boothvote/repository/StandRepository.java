package com.birkt.boothvote.repository;

import com.birkt.boothvote.entity.Stand;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface StandRepository extends JpaRepository<Stand, Long> {
    List<Stand> findByConferenceId(Long conferenceId);
    Optional<Stand> findByQrToken(String qrToken);
}
