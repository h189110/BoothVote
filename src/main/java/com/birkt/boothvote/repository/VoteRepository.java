package com.birkt.boothvote.repository;

import com.birkt.boothvote.entity.Vote;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface VoteRepository extends JpaRepository<Vote, Long> {
    List<Vote> findByStandId(Long id);

    List<Vote> findByConferenceId(Long conferenceId);

    boolean existsByStandIdAndGuestId(Long standId, String guestId);
}
