package com.trivia_api.demo.controller;

import com.trivia_api.demo.client.TriviaClient;
import com.trivia_api.demo.dto.QuestionResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TriviaController {

    private final TriviaClient triviaClient;

    public TriviaController(TriviaClient triviaClient) {
        this.triviaClient = triviaClient;
    }

    // GET http://localhost:8080/trivia-api?amount=10 --> Amount serve pra buscar uma quantidade de questão, sendo o default 10
    @GetMapping("/trivia-api")
    public QuestionResponse getTrivia(@RequestParam(defaultValue = "10") int amount) {
        return triviaClient.getTrivia(amount);
    }
}
