package com.trivia_api.demo.dto;

import com.trivia_api.demo.model.TriviaModel;

import java.util.List;

public record TriviaResquest(String category,
                             String type,
                             String difficulty,
                             String question,
                             String correct_answer,
                             List<String> incorrect_answers) {
}
