package com.trivia_api.demo.controller;

import com.trivia_api.demo.Service.QuestionService;
import com.trivia_api.demo.client.TriviaClient;
import com.trivia_api.demo.dto.*;
import com.trivia_api.demo.model.TriviaModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/trivia")
public class QuestionController {

    private final TriviaClient triviaClient;

    @Autowired
    private QuestionService questionService;

    public QuestionController(TriviaClient triviaClient) {
        this.triviaClient = triviaClient;
    }

    // Metodo GET para buscar as questões da API que está sendo consumida

    // GET http://localhost:8080/trivia/api?amount=10 --> Amount serve pra buscar uma quantidade de questão, sendo o default 10
    @GetMapping("/api")
    public BlockResponse getTrivia(@RequestParam(defaultValue = "10") int amount) {
        return triviaClient.getTrivia(amount);
    }


    // METODOS HTTP PARA ENVIAR/PUXAR/DELETAR E ATUALIZAR UM BLOCO DE QUESTÃO NO LOCAL

    // POST http://localhost:8080/trivia/local
    @PostMapping("/local")
    public ResponseEntity<Void> CriarBloco(@RequestBody BlockRequest questionRequest) {
        questionService.inserir(questionRequest);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    // GET http://localhost:8080/trivia/local/{id}
    @GetMapping("/local/{id}")
    public ResponseEntity<BlockResponse> BuscarBloco(@PathVariable long id) {
        var trivia = questionService.buscarId(id);

        return ResponseEntity.ok(trivia);
    }

    // GET http://localhost:8080/trivia/locals
    @GetMapping("locals")
    public ResponseEntity<List<BlockResponse>> BuscarTodos() {
        var trivias = questionService.buscarTodos();

        return ResponseEntity.ok(trivias);
    }

    // PUT http://localhost:8080/trivia/local/{id}
    @PutMapping("/local/{id}")
    public ResponseEntity<Void> AtualizarBloco(@PathVariable long id,
                                               @RequestBody BlockRequest questionRequest) {
        questionService.atualizarQuestao(id, questionRequest);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    // DELETE http://localhost:8080/trivia/local/{id}
    @DeleteMapping("/local/{id}")
    public ResponseEntity<Void> DeletarBloco(@PathVariable long id) {
        questionService.deletarQuestao(id);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    // METODOS HTTP PARA PUXAR/ADICIONAR/ATUALIZAR E DELETAR UMA QUESTÃO DE UM BLOCO

    // GET http://localhost:8080/trivia/local/{id}/question{idquestion}
    @GetMapping("/local/{id}/question/{idQuestion}")
    public ResponseEntity<TriviaModel> BuscarQuestão(@RequestParam long id,
                                                     @RequestParam long questionId) {
        TriviaModel question = questionService.buscarQuestaodobloco(id, questionId);
        return ResponseEntity.ok(question);
    }

    // PUT http://localhost:8080/trivia/local/{id}/question{idquestion}
    @PutMapping("/local/{id}/question/{idQuestion}")
    public ResponseEntity<Void> AtualizarQuestão(@PathVariable long id,
                                                 @PathVariable long idQuestion,
                                                 @RequestBody TriviaResponse triviaResponse) {
        questionService.atualizarQuestaoid(id, idQuestion, triviaResponse);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    // POST http://localhost:8080/trivia/local/{id}/question
    @PostMapping("/local/{id}/question")
    public ResponseEntity<Void> adicionarQuestaoAoBloco(
            @PathVariable long id,
            @RequestBody TriviaResquest triviaRequest
    ) {
        questionService.adicionarQuestaoAoBloco(id, triviaRequest);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    // DELETE http://localhost:8080/trivia/local/{id}/question{idquestion}
    @DeleteMapping("/local/{id}/question/{idQuestion}")
    public ResponseEntity<Void> DeletarQuestão(@RequestParam long id,
                                               @RequestParam long questionId) {
        questionService.deletarquestoaid(id, questionId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

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
