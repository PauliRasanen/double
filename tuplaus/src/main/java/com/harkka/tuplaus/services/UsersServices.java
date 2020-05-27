package com.harkka.tuplaus.services;

import java.util.Optional;

import com.harkka.tuplaus.model.Users;
import com.harkka.tuplaus.repository.UsersRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UsersServices {

    @Autowired
    private UsersRepository usersRepository;

    //Lisätään käyttäjä nimellä ja tilin alkuarvolla. Palauttaa käyttäjän idn. 
    public String addUser(String name, double balance) {
        Users user = new Users();
        user.setName(name);
        user.setBalance(balance);
        usersRepository.save(user);        
        return usersRepository.findByNameOrderByUserIdDesc(name).get(0).getUserId().toString();
    }

    //Poistetaan käyttäjä idllä.
    //Palauttaa "Käyttäjä poistettu".
    public String removeUser(Integer userId) {
        usersRepository.deleteById(userId);
        return "Käyttäjä poistettu";
    }

    //Hakee idllä käyttäjän tiedot.
    //Palauttaa idn, nimen ja tilin saldon.
    public Optional<Users> userDetails(Integer userId) {
        return usersRepository.findById(userId);
    }
    //Hakee käyttäjän idllä ja palauttaa tilin saldon.
    public double getBalance(Integer userId) {
        return usersRepository.findById(userId).get().getBalance();
    }

    //Asettaa tilin saldon idllä ja saldon määrällä.
    public void setBalance(Integer userId, double balance) {
        Users user = usersRepository.findById(userId).get();
        user.setBalance(balance);
        usersRepository.save(user);
    }
    
}