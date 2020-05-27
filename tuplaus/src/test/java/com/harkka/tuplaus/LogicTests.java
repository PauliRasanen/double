package com.harkka.tuplaus;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@TestMethodOrder(OrderAnnotation.class)
public class LogicTests {

    private static String userId = null;

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate testRestTemplate;

    //Testataan käyttäjän lisäystä.
    @Test
    @Order(1)
    public void registeringUserTesting() {

        boolean allgood = false;

        ResponseEntity<String> responseEntity = testRestTemplate.postForEntity(
                "http://localhost:" + port + "/adduser?name=Testinimi&balance=1000.00", null, String.class);
        setUserId(responseEntity.getBody().toString());
        if (getUserId() != null) {
            allgood = true;
        }

        assertEquals("true", Boolean.toString(allgood));
    }

    //Testataan tuplausta.
    @Test
    @Order(2)
    public void doubleUpTest() {
        ResponseEntity<String> responseEntity = testRestTemplate.postForEntity(
                "http://localhost:" + port + "/doubleup?userId=" + getUserId() + "&betAmount=10&playersChoice=pieni", null,
                String.class);
        assertEquals("true", Boolean.toString(responseEntity.getBody().contains("winningAmount")));
    }

    //Testataan tuplausta voiton jälkeen.
    @Test
    @Order(3)
    public void doubleUpAgainTest() {

        boolean allgood = false;

        ResponseEntity<String> responseEntity = testRestTemplate.postForEntity(
                "http://localhost:" + port + "/doubleagain?userId=" + getUserId() + "&playersChoice=pieni", null,
                String.class);
        if (responseEntity.getBody().contains("winningAmount")
                || responseEntity.getBody().equals("Viimeinen tuplaus ei mennyt oikein")) {
            allgood = true;
        }
        assertEquals("true", Boolean.toString(allgood));
    }

    //Testataan voittojen kotiuttamista.
    @Test
    @Order(4)
    public void collectWinnings() {

        ResponseEntity<String> responseEntity = testRestTemplate
                .postForEntity("http://localhost:" + port + "/collectwinnings?userId=" + getUserId(), null, String.class);
        assertEquals("Voitot lisätty", responseEntity.getBody());
    }

    //Testataan käyttäjän poistoa ja samalla poistetaan testissä luotu käyttäjä.
    @Test
    @Order(5)
    public void removeUser() {

        ResponseEntity<String> responseEntity = testRestTemplate
                .postForEntity("http://localhost:" + port + "/removeuser?userId=" + getUserId(), null, String.class);
        assertEquals("Käyttäjä poistettu", responseEntity.getBody());
    }

    private String getUserId() {
        return userId;
    }

    private void setUserId(String userId) {
        LogicTests.userId = userId;
    }

    

}