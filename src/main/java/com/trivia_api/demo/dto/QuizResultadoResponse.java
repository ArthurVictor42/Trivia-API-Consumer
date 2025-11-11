package com.trivia_api.demo.dto;

import java.util.List;

public record QuizResultadoResponse(
        int totalPerguntas,
        int acertos,
        int erros,
        List<ResultadoTotal> detalhes
) {
    public record ResultadoTotal(String question,
                                 String respostaUser,
                                 String respostaCorreta,
                                 boolean acertou){
    }
}
