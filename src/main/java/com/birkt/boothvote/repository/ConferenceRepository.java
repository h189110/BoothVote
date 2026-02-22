package com.birkt.boothvote.repository;


import com.birkt.boothvote.entity.Conference;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface ConferenceRepository extends JpaRepository<Conference, Long> {
    List<Conference> findAllByVoteEndTimeBeforeAndEmailSentFalse(LocalDateTime now);
}