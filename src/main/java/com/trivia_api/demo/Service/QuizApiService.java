package com.trivia_api.demo.Service;

import com.trivia_api.demo.client.TriviaClient;
import com.trivia_api.demo.dto.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class QuizApiService {

    @Autowired
    private TriviaClient triviaClient;

    private final Map<String, List<TriviaResponse>> rodadas = new HashMap<>();

    // Inicia uma rodada de perguntas diretamente da API
    public Map<String, Object> iniciarRodadaApi(int quantidade) {
        // Chama a API
        BlockResponse response = triviaClient.getTrivia(quantidade);

        // Cria um ID unico para a rodada
        String rodada = UUID.randomUUID().toString();
        rodadas.put(rodada, response.results());

        // Monta uma lista de pergunta sem as respostas certa
        List<QuizPerguntaResponse> pergunta = new ArrayList<>();
        long idcont = 1;

        for (TriviaResponse trivia : response.results()) {
            List<String> opcoes = new ArrayList<>(trivia.incorrect_answers());
            opcoes.add(trivia.correct_answer());
            Collections.shuffle(opcoes);

            pergunta.add(new QuizPerguntaResponse(
                    idcont++,
                    trivia.category(),
                    trivia.type(),
                    trivia.difficulty(),
                    trivia.question(),
                    opcoes
            ));
        }

        // Retorna o id da rodada
        Map<String, Object> resultados = new HashMap<>();
        resultados.put("Id: ", rodada);
        resultados.put("Pergunta: ", pergunta);

        return resultados;
    }

    // Corrigi as respostas
    public QuizResultadoResponse corrigirRodada(String rodada, List<QuizRespostaRequest> respostas) {
        // Recupera as perguntas da rodada
        List<TriviaResponse> perguntas = rodadas.get(rodada);

        if (perguntas == null) {
            throw new RuntimeException("Rodada não encontrada ou expirada");
        }

        int acertos = 0;
        List<QuizResultadoResponse.ResultadoTotal> detalhes = new ArrayList<>();

        for (QuizRespostaRequest r : respostas) {
            TriviaResponse questao = getPerguntaPorIndice(perguntas, r.idPergunta());

            if (questao == null) {
                throw new RuntimeException("Pergunta não encontrada.");
            }

            String respostaUsuario = r.resposta() != null ? r.resposta() : "";
            String respostaCorreta = questao.correct_answer() != null ? questao.correct_answer() : "";

            boolean acertou = respostaCorreta.equalsIgnoreCase(respostaUsuario);
            if (acertou) {
                acertos++;
            }

            detalhes.add(new QuizResultadoResponse.ResultadoTotal(
                    questao.question(),
                    respostaUsuario,
                    respostaCorreta,
                    acertou
            ));
        }

        int total = respostas.size();
        int erros = total - acertos;

        // Remove a rodada após corrigir
        rodadas.remove(rodada);

        return new QuizResultadoResponse(total, acertos, erros, detalhes);
    }

    // Busca pergunta pelo ID
    private TriviaResponse getPerguntaPorIndice(List<TriviaResponse> perguntas, Long idPergunta) {
        if (idPergunta == null || idPergunta < 1 || idPergunta > perguntas.size()) {
            return null;
        }
        return perguntas.get(idPergunta.intValue() - 1);
    }
}
