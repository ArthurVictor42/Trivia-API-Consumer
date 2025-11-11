package com.trivia_api.demo.dto;

public record QuizRespostaRequest(
        Long idPergunta,
        String resposta
) {
}
