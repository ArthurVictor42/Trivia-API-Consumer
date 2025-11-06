package com.trivia_api.demo.model;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class QuestionModel {
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
