package com.trivia_api.demo.model;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@ToString
public class QuestionModel {

    private String type;
    private String difficulty;
    private String category;
    private String question;
    private List<String> answers;
    private String corret;

}
