package com.trivia_api.demo.controller;

import com.trivia_api.demo.Service.QuestionService;
import com.trivia_api.demo.dto.QuizPerguntaResponse;
import com.trivia_api.demo.dto.QuizRespostaRequest;
import com.trivia_api.demo.dto.QuizResultadoResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/trivia")
public class QuizTriviaController {
    @Autowired
    private QuestionService questionService;

    // Metodos de rodadas de questão!
    // GET http://localhost:8080/trivia/local/1/quiz/start?quantidade=3&difficulty=fácil ---> Exemplo de envio
    @GetMapping("/local/{id}/quiz/start")
    public ResponseEntity<List<QuizPerguntaResponse>> iniciarRodada(
            @RequestParam long id,
            @RequestParam(defaultValue = "5") int quantidade,
            @RequestParam(required = false) String difficulty) {

        var perguntas = questionService.iniciarRodada(id, quantidade, difficulty);

        return ResponseEntity.ok(perguntas);

    }

    // POST http://localhost:8080/trivia/local/1/quiz/submit
    @PostMapping("/local/{id}/quiz/submit")
    public ResponseEntity<QuizResultadoResponse> responderRodada(@PathVariable long id,
                                                                 @RequestBody List<QuizRespostaRequest> respostas){
        var resultado = questionService.corrigirRodada(id, respostas);

        return ResponseEntity.ok(resultado);
    }
}
