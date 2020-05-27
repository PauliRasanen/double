package com.harkka.tuplaus.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import javax.persistence.Table;
import javax.persistence.GenerationType;

@Entity
@Table(name = "DOUBLINGROUNDS")
public class DoublingRounds {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "timestamp")
    private String timestamp;

    @Column(name = "userId")
    private int userId;

    @Column(name = "betAmount")
    private double betAmount;

    @Column(name = "playersChoice")
    private String playersChoice;

    @Column(name = "randomisedCard")
    private Integer randomisedCard;

    @Column(name = "roundWon")
    private boolean roundWon;

    @Column(name = "WinningAmount")
    private Double WinningAmount;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public double getBetAmount() {
        return betAmount;
    }

    public void setBetAmount(double betAmount) {
        this.betAmount = betAmount;
    }

    public String getplayersChoice() {
        return playersChoice;
    }

    public void setplayersChoice(String playersChoice) {
        this.playersChoice = playersChoice;
    }

    public Integer getRandomisedCard() {
        return randomisedCard;
    }

    public void setRandomisedCard(Integer randomisedCard) {
        this.randomisedCard = randomisedCard;
    }

    public boolean isRoundWon() {
        return roundWon;
    }

    public void setRoundWon(boolean roundWon) {
        this.roundWon = roundWon;
    }

    public Double getWinningAmount() {
        return WinningAmount;
    }

    public void setWinningAmount(Double winningAmount) {
        WinningAmount = winningAmount;
    }

}