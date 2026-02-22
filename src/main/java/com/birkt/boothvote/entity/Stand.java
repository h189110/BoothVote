package com.birkt.boothvote.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "stands")
public class Stand {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String firmaNavn;

    private String info;

    private String bildeUrl;

    private String positionIdentifier;

    @Column(unique = true, nullable = false)
    private String qrToken;

    @ManyToOne
    @JoinColumn(name = "conference_id")
    private Conference conference;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirmaNavn() {
        return firmaNavn;
    }

    public void setFirmaNavn(String firmaNavn) {
        this.firmaNavn = firmaNavn;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getBildeUrl() {
        return bildeUrl;
    }

    public void setBildeUrl(String bildeUrl) {
        this.bildeUrl = bildeUrl;
    }

    public String getPositionIdentifier() {
        return positionIdentifier;
    }

    public void setPositionIdentifier(String positionIdentifier) {
        this.positionIdentifier = positionIdentifier;
    }

    public Conference getConference() {
        return conference;
    }

    public void setConference(Conference conference) {
        this.conference = conference;
    }

    public String getQrToken() {
        return qrToken;
    }

    public void setQrToken(String qrToken) {
        this.qrToken = qrToken;
    }
}
