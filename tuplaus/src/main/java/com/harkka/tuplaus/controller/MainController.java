package com.harkka.tuplaus.controller;

import java.util.Optional;

import com.harkka.tuplaus.model.Users;
import com.harkka.tuplaus.services.DoublingService;
import com.harkka.tuplaus.services.UsersServices;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MainController {

    @Autowired
    private UsersServices usersServices;

    @Autowired
    private DoublingService doublingService;

    //Lisätään käyttäjä nimellä ja tilin alkuarvolla. Palauttaa käyttäjän idn.
    @PostMapping(path = "/adduser")
    public String addUser (@RequestParam String name, @RequestParam double balance) {
        return usersServices.addUser(name, balance);
    }

    //Poistetaan käyttäjä idllä.
    //Palauttaa "käyttäjä poistettu".
    @PostMapping(path = "/removeuser")
    public String removeUser (@RequestParam Integer userId) {
        return usersServices.removeUser(userId);
    }

    //Tuplaus toiminto johon syötetään id, panoksen arvo ja käyttäjän valintana "pieni" tai "iso". Tarkistaa onko tilillä katetta, arpoo kortin ja tarkistaa oliko tuplaus onnistunut.
    //Vähentää käyttäjän tililtä panoksen verran.
    //Palauttaa idn, arvotun kortin, menikö tuplaus oikein, voiton arvon (0 jos meni väärin).
    @PostMapping(path = "/doubleup") 
    public String doubleup (@RequestParam Integer userId, @RequestParam double betAmount, @RequestParam String playersChoice) {
        double accountbalance = usersServices.getBalance(userId);
        if (accountbalance<betAmount)
            return "Virhe, saldo ei riitä";
        usersServices.setBalance(userId, accountbalance-betAmount);
        Integer randomisedCard = doublingService.drawcard();
        double winningAmount = 0;
        if (doublingService.doubleResult(playersChoice, randomisedCard).equals("true")) {
            winningAmount = betAmount *2;
            doublingService.addDoubling(userId, betAmount, playersChoice, randomisedCard, winningAmount, true);
            return doublingService.results(userId, randomisedCard, true, winningAmount).toString(); 
        }
        doublingService.addDoubling(userId, betAmount, playersChoice, randomisedCard, winningAmount, false);
        return doublingService.results(userId, randomisedCard, false, winningAmount).toString();
    }

    //Uudelleen tuplaus jos aikaisempi tuplaus meni oikein. Syötetään id sekä käyttäjän valinta "pieni" tai "iso". Ottaa käyttäjän edellisen tuplauksen voiton arvon ja käyttää sitä panoksena.
    //Jos aikaisempi tuplaus oli mennyt väärin palauttaa "Viimeinen tuplaus ei mennyt oikein".
    //Palauttaa idn, arvotun kortin, menikö tuplaus oikein, voiton arvon (0 jos meni väärin).
    @PostMapping(path = "/doubleagain")
    public String doubleAgain(@RequestParam Integer userId, @RequestParam String playersChoice) {
        double betAmount = doublingService.lastWonAmount(userId);
        if (betAmount!=0) {
            Integer randomisedCard = doublingService.drawcard();
            double winningAmount = 0;
            if (doublingService.doubleResult(playersChoice, randomisedCard).equals("true")) {
                winningAmount = betAmount *2;
                doublingService.addDoubling(userId, betAmount, playersChoice, randomisedCard, winningAmount, true);
                return doublingService.results(userId, randomisedCard, true, winningAmount).toString();
        }
        doublingService.addDoubling(userId, betAmount, playersChoice, randomisedCard, winningAmount, false);
        return doublingService.results(userId, randomisedCard, false, winningAmount).toString();
        }
        return "Viimeinen tuplaus ei mennyt oikein";
    }

    //Voittojen kotiutus idllä. Etsii käyttäjän viimeisen tuplauksen ja sen voiton määrän. Lisää voiton verran käyttäjän tilille.
    //Palauttaa "Voitot lisätty"
    @PostMapping(path = "/collectwinnings")
    public String collectWinnings(@RequestParam Integer userId) {
        double betAmount = doublingService.lastWonAmount(userId);
        double accountbalance = usersServices.getBalance(userId);
        usersServices.setBalance(userId, accountbalance+betAmount);
        doublingService.collect(userId);
        return "Voitot lisätty";
    }

    //Hakee idllä käyttäjän tiedot.
    //Palauttaa idn, nimen ja tilin saldon.
    @GetMapping(path = "/userdetails")
    public Optional<Users> userdetails (@RequestParam Integer userId) { 
        return usersServices.userDetails(userId);
    }

    
}