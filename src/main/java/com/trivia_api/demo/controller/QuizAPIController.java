package com.trivia_api.demo.controller;

import com.trivia_api.demo.Service.QuestionService;
import com.trivia_api.demo.Service.QuizApiService;
import com.trivia_api.demo.dto.QuizPerguntaResponse;
import com.trivia_api.demo.dto.QuizRespostaRequest;
import com.trivia_api.demo.dto.QuizResultadoResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/trivia/api/quiz")
public class QuizAPIController {
    private final QuizApiService quizApiService;

    public  QuizAPIController(QuizApiService quizApiService) {
        this.quizApiService = quizApiService;
    }

    // GET para iniciar uma rodada usando as perguntas da API Externa
    @GetMapping("/start")
    public ResponseEntity<Map<String, Object>> iniciarRodadaAPI(
            @RequestParam(defaultValue = "5") int quantidade){

        var perguntas = quizApiService.iniciarRodadaApi(quantidade);
        return ResponseEntity.ok(perguntas);
    }

    // POST para enviar e recebe o resultado da rodada
    @PostMapping("/submit")
    public ResponseEntity<QuizResultadoResponse> responderRodada(
            @RequestParam String rodadaId,
            @RequestBody List<QuizRespostaRequest> respostas) {

        QuizResultadoResponse resultado = quizApiService.corrigirRodada(rodadaId, respostas);
        return ResponseEntity.ok(resultado);
    }
}
