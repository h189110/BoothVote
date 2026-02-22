package com.birkt.boothvote.service;


import com.birkt.boothvote.entity.Stand;
import com.birkt.boothvote.repository.ConferenceRepository;
import com.birkt.boothvote.repository.StandRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class StandService {

    private final StandRepository standRepository;
    private final ConferenceRepository conferenceRepository;

    public StandService(StandRepository standRepository, ConferenceRepository conferenceRepository) {
        this.standRepository = standRepository;
        this.conferenceRepository = conferenceRepository;
    }



    //create
    public Stand createStand(Long conferenceId, String firmaNavn, String info,
                             String bildeUrl, String positionIdentifier) {

        var conference = conferenceRepository.findById(conferenceId)
                .orElseThrow(() -> new RuntimeException("Conference not found"));

        Stand stand = new Stand();
        stand.setFirmaNavn(firmaNavn);
        stand.setInfo(info);
        stand.setBildeUrl(bildeUrl);
        stand.setPositionIdentifier(positionIdentifier);
        stand.setConference(conference);

        //  Generer unik token
        stand.setQrToken(UUID.randomUUID().toString());

        return standRepository.save(stand);
    }

    //read
    public List<Stand> getStandsForConference(Long conferenceId) {
        return standRepository.findByConferenceId(conferenceId);
    }
    public Stand getStandById(Long standId) {
        return standRepository.findById(standId)
                .orElseThrow(() -> new RuntimeException("Stand not found"));
    }
    public Stand getStandByQrToken(String token) {
        return standRepository.findByQrToken(token)
                .orElseThrow(() -> new RuntimeException("Invalid QR token"));
    }

    // UPDATE
    public Stand updateStand(Long standId, String firmaNavn, String info, String bildeUrl, String positionIdentifier) {
        Stand stand = standRepository.findById(standId)
                .orElseThrow(() -> new RuntimeException("Stand not found"));

        if (firmaNavn != null) stand.setFirmaNavn(firmaNavn);
        if (info != null) stand.setInfo(info);
        if (bildeUrl != null) stand.setBildeUrl(bildeUrl);
        if (positionIdentifier != null) stand.setPositionIdentifier(positionIdentifier);

        return standRepository.save(stand);
    }

    // DELETE
    public void deleteStand(Long standId) {
        Stand stand = standRepository.findById(standId)
                .orElseThrow(() -> new RuntimeException("Stand not found"));
        standRepository.delete(stand);
    }



}
