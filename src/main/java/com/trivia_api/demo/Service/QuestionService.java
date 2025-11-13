package com.trivia_api.demo.Service;

import com.trivia_api.demo.Repository.QuestionRepository;
import com.trivia_api.demo.dto.*;
import com.trivia_api.demo.model.TriviaModel;
import com.trivia_api.demo.model.QuestionModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class QuestionService {

    @Autowired
    private QuestionRepository questionRepository;


    // Metodos paras os bloco

    // Salva um novo conjunto de perguntas no banco
    public void inserir(BlockRequest questionRequest) {
        QuestionModel model = new QuestionModel();
        model.setResponse_code(questionRequest.response_code());

        List<TriviaModel> questions = questionRequest.results().stream()
                .map(q -> new TriviaModel(
                        null,
                        q.category(),
                        q.type(),
                        q.difficulty(),
                        q.question(),
                        q.correct_answer(),
                        q.incorrect_answers()
                ))
                .toList();

        model.setResults(questions);
        questionRepository.save(model);
    }

    // Busca um registro pelo ID e converte de volta pra QuestionResponse
    public BlockResponse buscarId(long id) {
        return questionRepository.findById(id)
                .map(model -> new BlockResponse(
                        model.getResponse_code(),
                        model.getResults().stream()
                                .map(q -> new TriviaResponse(
                                        q.getCategory(),
                                        q.getType(),
                                        q.getDifficulty(),
                                        q.getQuestion(),
                                        q.getCorrect_answer(),
                                        q.getIncorrect_answers()
                                ))
                                .toList()
                ))
                .orElseThrow(() -> new RuntimeException("Bloco não encontrada."));
    }

    // Retorna todos os registros do banco convertidos em QuestionResponse
    public List<BlockResponse> buscarTodos() {
        return questionRepository.findAll()
                .stream()
                .map(model -> new BlockResponse(
                        model.getResponse_code(),
                        model.getResults().stream()
                                .map(q -> new TriviaResponse(
                                        q.getCategory(),
                                        q.getType(),
                                        q.getDifficulty(),
                                        q.getQuestion(),
                                        q.getCorrect_answer(),
                                        q.getIncorrect_answers()
                                ))
                                .toList()
                ))
                .toList();
    }

    // Atualiza um registro existente com novas perguntas
    public void atualizarQuestao(long id, BlockRequest questionRequest) {
        QuestionModel model = questionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Bloco não encontrada."));

        model.setResponse_code(questionRequest.response_code());

        // Limpa as perguntas antigas e adiciona as novas
        model.getResults().clear();

        questionRequest.results().forEach(q -> {
            TriviaModel question = new TriviaModel();
            question.setCategory(q.category());
            question.setType(q.type());
            question.setDifficulty(q.difficulty());
            question.setQuestion(q.question());
            question.setCorrect_answer(q.correct_answer());
            question.setIncorrect_answers(new ArrayList<>(q.incorrect_answers()));

            model.getResults().add(question);
        });

        questionRepository.save(model);
    }


    // Deleta um registro do banco pelo ID
    public void deletarQuestao(long id) {
        QuestionModel model = questionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Bloco não encontrada."));

        questionRepository.delete(model);
    }

    //Metodos das questões individuais

    public TriviaModel buscarQuestaodobloco(long id, long questionId) {
        // Buscar o bloco da questão
        QuestionModel bloco = questionRepository.findById(id).
                orElseThrow(() -> new RuntimeException("Bloco de Questão não encontrada"));

        // Buscar a questão
        return bloco.getResults().stream().
                filter(q -> q.getId().equals(questionId)).findFirst().
                orElseThrow(() -> new RuntimeException("Questão não encontrada nesse bloco"));
    }

    public void atualizarQuestaoid(long id, long idQuestion, TriviaResponse triviaResponse) {
        // Buscar o bloco da questão
        QuestionModel bloco = questionRepository.findById(id).
                orElseThrow(() -> new RuntimeException("Bloco de questão não encontrada"));

        // Buscar a questão
        TriviaModel question = bloco.getResults().stream()
                .filter(q -> q.getId().equals(idQuestion))
                .findFirst().orElseThrow(() -> new RuntimeException("Questão não encontrada"));

        //atualizar as informações
        question.setCategory(triviaResponse.category());
        question.setType(triviaResponse.type());
        question.setDifficulty(triviaResponse.difficulty());
        question.setQuestion(triviaResponse.question());
        question.setCorrect_answer(triviaResponse.correct_answer());
        question.setIncorrect_answers(triviaResponse.incorrect_answers());

        // Salva as informações novas no bloco
        questionRepository.save(bloco);

    }

    public void deletarquestoaid(long id, long questionId) {
        QuestionModel bloco = questionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Bloco não encontrado."));

        // Remove ao encontra a questão no bloco
        bloco.getResults().removeIf(q -> q.getId().equals(questionId));

        questionRepository.save(bloco);
    }

    // Adiciona uma nova questão ao bloco escolhido
    public void adicionarQuestaoAoBloco(long id, TriviaResquest triviaRequest) {
        QuestionModel bloco = questionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Bloco não encontrado."));

        TriviaModel novaQuestao = new TriviaModel();
        novaQuestao.setCategory(triviaRequest.category());
        novaQuestao.setType(triviaRequest.type());
        novaQuestao.setDifficulty(triviaRequest.difficulty());
        novaQuestao.setQuestion(triviaRequest.question());
        novaQuestao.setCorrect_answer(triviaRequest.correct_answer());
        novaQuestao.setIncorrect_answers(triviaRequest.incorrect_answers());

        bloco.getResults().add(novaQuestao);
        questionRepository.save(bloco);
    }

    // Exibe Uma quantidade N de perguntas na rodada
    public List<QuizPerguntaResponse> iniciarRodada(long id, int quantidade, String difficulty) {
        QuestionModel bloco = questionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Bloco não encontrado."));

        // Filtra por dificuldade (se for informada)
        Stream<TriviaModel> stream = bloco.getResults().stream();
        if (difficulty != null && !difficulty.isBlank()) {
            stream = stream.filter(q -> q.getDifficulty().equalsIgnoreCase(difficulty));
        }

        // Limita e coleta as perguntas
        List<TriviaModel> perguntas = stream
                .limit(quantidade)
                .collect(Collectors.toList());

        // Monta o DTO de exibição (sem resposta correta)
        return perguntas.stream().map(q -> {
            List<String> opcoes = new ArrayList<>(q.getIncorrect_answers());
            opcoes.add(q.getCorrect_answer());
            Collections.shuffle(opcoes);

            return new QuizPerguntaResponse(
                    q.getId(),
                    q.getCategory(),
                    q.getType(),
                    q.getDifficulty(),
                    q.getQuestion(),
                    opcoes
            );
        }).toList();
    }

    public QuizResultadoResponse corrigirRodada(long id, List<QuizRespostaRequest> respostas) {
        QuestionModel bloco = questionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Bloco não encontrado."));

        int acertos = 0;
        List<QuizResultadoResponse.ResultadoTotal> detalhes = new ArrayList<>();

        for (QuizRespostaRequest r : respostas) {
            TriviaModel questao = bloco.getResults().stream()
                    .filter(q -> q.getId().equals(r.idPergunta()))
                    .findFirst()
                    .orElseThrow(() -> new RuntimeException("Pergunta não encontrada."));

            String respostaUsuario = r.resposta() != null ? r.resposta() : "";
            String respostaCorreta = questao.getCorrect_answer() != null ? questao.getCorrect_answer() : "";

            boolean acertou = respostaCorreta.equalsIgnoreCase(respostaUsuario);
            if (acertou) {
                acertos++;
            }

            detalhes.add(new QuizResultadoResponse.ResultadoTotal(
                    questao.getQuestion(),
                    respostaUsuario,
                    respostaCorreta,
                    acertou
            ));
        }

        int total = respostas.size();
        int erros = total - acertos;

        return new QuizResultadoResponse(total, acertos, erros, detalhes);
    }
}
