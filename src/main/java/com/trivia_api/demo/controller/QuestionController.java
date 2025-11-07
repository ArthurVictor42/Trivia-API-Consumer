package com.trivia_api.demo.controller;

import com.trivia_api.demo.Repository.QuestionRepository;
import com.trivia_api.demo.Service.QuestionService;
import com.trivia_api.demo.client.TriviaClient;
import com.trivia_api.demo.dto.QuestionResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class QuestionController {

    private final TriviaClient triviaClient;

    @Autowired
    private QuestionService questionService;

    public QuestionController(TriviaClient triviaClient) {
        this.triviaClient = triviaClient;
    }

    // GET http://localhost:8080/trivia-api?amount=10 --> Amount serve pra buscar uma quantidade de questão, sendo o default 10
    @GetMapping("/trivia-api")
    public QuestionResponse getTrivia(@RequestParam(defaultValue = "10") int amount) {
        return triviaClient.getTrivia(amount);
    }


    // POST http://localhost:8080/trivia-local
    @PostMapping("/trivia-local")
    public ResponseEntity<Void> criarQuestões(@RequestBody QuestionResponse questionRequest) {
        questionService.inserir(questionRequest);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    // GET http://localhost:8080/trivia-local/{id}
    @GetMapping("/trivia-local/{id}")
    public ResponseEntity<QuestionResponse> buscarQuestão(@PathVariable long id) {
        var trivia = questionService.buscarId(id);

        return ResponseEntity.ok(trivia);
    }

    // GET http://localhost:8080/trivia-locals
    @GetMapping("trivia-locals")
    public ResponseEntity<List<QuestionResponse>> buscarTodos() {
        var trivias = questionService.buscarTodos();

        return ResponseEntity.ok(trivias);
    }

    // PUT http://localhost:8080/trivia-local/{id}
    @PutMapping("/trivia-local/{id}")
    public ResponseEntity<Void> atualizarQuestão(@PathVariable long id, @RequestBody QuestionResponse questionRequest) {
        questionService.atualizarQuestao(id, questionRequest);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    // DELETE http://localhost:8080/trivia-local/{id}
    @DeleteMapping("/trivia-local/{id}")
    public ResponseEntity<Void> deletarQuestão(@PathVariable long id) {
        questionService.deletarQuestao(id);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
