package com.harkka.tuplaus.services;

import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.harkka.tuplaus.model.DoublingRounds;
import com.harkka.tuplaus.repository.DoublingRoundsRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DoublingService {
    @Autowired
    private DoublingRoundsRepository doublingRoundsRepository;

    @Autowired
    private UsersServices usersServices;

    //Arpoo arvon 1-13 ja palauttaa sen.
    public Integer drawcard() {
        return (int)(Math.random()*13+1);
    }

    //Tarkastaa menikö tuplaus oikein. Syötetään pelaajan valinta ja arvottu kortti.
    //Palauttaa "true" tai "false".
    public String doubleResult(String playersChoice, Integer drawedCard) {
        if (drawedCard==7 || (drawedCard<=6 && playersChoice.equals("suuri")) || (drawedCard>=8 && playersChoice.equals("pieni"))) {
            return "false";
        }
        return "true";
    }

    //Lisätään tuplauksen tiedot kantaan. Sisään id, panos, pelaajan valinta, arvottu kortti, voiton määrä ja voittiko kierros.
    public void addDoubling(Integer userId, double betAmount, String playersChoice, Integer randomisedCard, double winningAmount, boolean roundWon) {
        DoublingRounds doublingRound = new DoublingRounds();
        Date date = new Date();
        Timestamp ts = new Timestamp(date.getTime());
        doublingRound.setTimestamp(ts.toString());
        doublingRound.setUserId(userId);
        doublingRound.setBetAmount(betAmount);
        doublingRound.setplayersChoice(playersChoice);
        doublingRound.setRandomisedCard(randomisedCard);
        doublingRound.setWinningAmount(winningAmount);
        doublingRound.setRoundWon(roundWon);
        doublingRoundsRepository.save(doublingRound);
    }

    //Etsii käyttäjän pelaaman viimeisen kierroksen voiton määrän ja palauttaa sen.
    public double lastWonAmount(Integer userId) {
        return doublingRoundsRepository.findByUserIdOrderByIdDesc(userId).get(0).getWinningAmount();
    }

    //Lisää käyttäjän viimeisen kierroksen voiton määrän tilille ja sen nollaa voiton määrän.
    public void collect(Integer userId) {
        DoublingRounds doublingRounds = doublingRoundsRepository.findByUserIdOrderByIdDesc(userId).get(0);
        doublingRounds.setWinningAmount(0.0);
        doublingRoundsRepository.save(doublingRounds);
    }

    //Luo HashMapin kierroksen tiedoista jotka lähetetään peli-clientille. Sisään käyttäjän id, arvottu kortti, voittiko kierros, voiton määrä.
    //Palauttaa arvotun kortin, voittiko kierros, voitonmäärän ja tilin saldon.
    public Map<String, String> results(Integer userId, Integer randomisedCard, boolean roundWon, double winningAmount) {
        HashMap<String, String> map = new HashMap<>();
        map.put("randomisedCard", randomisedCard.toString());
        map.put("roundWon", String.valueOf(roundWon));
        map.put("winningAmount", Double.toString(winningAmount));
        map.put("balance", Double.toString(usersServices.getBalance(userId)));
        return map;
    }

}