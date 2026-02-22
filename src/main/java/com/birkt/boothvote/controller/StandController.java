package com.birkt.boothvote.controller;

import com.birkt.boothvote.DTO.StandDto;
import com.birkt.boothvote.entity.Conference;
import com.birkt.boothvote.entity.Stand;
import com.birkt.boothvote.repository.StandRepository;
import com.birkt.boothvote.service.ConferenceService;
import com.birkt.boothvote.service.StandService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/stands")
public class StandController {

    private final StandService standService;
    private final ConferenceService conferenceService;

    public StandController(StandService standService, ConferenceService conferenceService) {
        this.standService = standService;
        this.conferenceService = conferenceService;
    }



    //intended most used for qr code
    @GetMapping("/scan/{token}")
    public ResponseEntity<Stand> scanStand(@PathVariable String token) {
        Stand stand = standService.getStandByQrToken(token);
        return ResponseEntity.ok(stand);
    }

    @PostMapping
    public ResponseEntity<Stand> createStand(@RequestBody StandDto dto) {
        Stand stand = standService.createStand(dto.conferenceId(), dto.firmaNavn(), dto.info(),
                dto.bildeUrl(), dto.positionIdentifier());
        return ResponseEntity.ok(stand);
    }

    @GetMapping("/current")
    public ResponseEntity<List<Stand>> getCurrentConferenceStands() {
        Conference current = conferenceService.getCurrentConference();
        List<Stand> stands = standService.getStandsForConference(current.getId());
        return ResponseEntity.ok(stands);
    }

    @GetMapping("/conference/{conferenceId}")
    public ResponseEntity<List<Stand>> getStandsByConferenceId(@PathVariable Long conferenceId) {
        List<Stand> stands = standService.getStandsForConference(conferenceId);
        return ResponseEntity.ok(stands);
    }

    // Get stand by ID
    @GetMapping("/{standId}")
    public ResponseEntity<Stand> getStandById(@PathVariable Long standId) {
        Stand stand = standService.getStandById(standId);
        return ResponseEntity.ok(stand);
    }

    @PutMapping("/{standId}")
    public ResponseEntity<Stand> updateStand(@PathVariable Long standId, @RequestBody StandDto dto) {
        Stand stand = standService.updateStand(standId, dto.firmaNavn(), dto.info(),
                dto.bildeUrl(), dto.positionIdentifier());
        return ResponseEntity.ok(stand);
    }

    @DeleteMapping("/{standId}")
    public ResponseEntity<Void> deleteStand(@PathVariable Long standId) {
        standService.deleteStand(standId);
        return ResponseEntity.noContent().build();
    }
}