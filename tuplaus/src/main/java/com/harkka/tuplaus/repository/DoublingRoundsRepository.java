package com.harkka.tuplaus.repository;

import java.util.List;

import com.harkka.tuplaus.model.DoublingRounds;

import org.springframework.data.repository.CrudRepository;

public interface DoublingRoundsRepository extends CrudRepository<DoublingRounds, Integer> {

    //Hakee listan käyttäjistä käyttäjän idllä ja järjestää ne uusin ensin.
    List<DoublingRounds> findByUserIdOrderByIdDesc(int userId); 
}