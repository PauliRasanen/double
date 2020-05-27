package com.harkka.tuplaus.repository;

import java.util.List;

import com.harkka.tuplaus.model.Users;

import org.springframework.data.repository.CrudRepository;

public interface UsersRepository extends CrudRepository<Users, Integer> {

    //Hakee listan käyttäjistä käyttäjän nimellä ja järjestää ne uusin ensin.
    List<Users> findByNameOrderByUserIdDesc(String name);
}