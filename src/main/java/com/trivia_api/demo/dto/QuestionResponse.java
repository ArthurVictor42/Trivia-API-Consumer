package com.trivia_api.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class QuestionResponse {
    private int response_code;
    private List<Question> results;


    @Getter
    @Setter
    @AllArgsConstructor
    public static class Question {
        private String category;
        private String type;
        private String difficulty;
        private String question;
        private String correct_answer;
        private List<String> incorrect_answers;
    }
}
