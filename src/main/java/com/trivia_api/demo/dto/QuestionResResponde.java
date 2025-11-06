package com.trivia_api.demo.dto;

import java.util.List;

public record QuestionResResponde(String category,
                                  String type,
                                  String difficulty,
                                  String question,
                                  String correct_answer,
                                  List<String> incorrect_answers) {
}
