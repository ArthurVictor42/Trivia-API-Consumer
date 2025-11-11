package com.trivia_api.demo.dto;

import java.util.List;

public record QuizPerguntaResponse(Long id,
                                   String category, String type,
                                   String difficulty,
                                   String question,
                                   List<String> options) {
}
