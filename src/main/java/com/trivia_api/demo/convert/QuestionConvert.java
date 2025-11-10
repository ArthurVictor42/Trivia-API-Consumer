package com.trivia_api.demo.convert;

import com.trivia_api.demo.dto.QuestionResponse;
import com.trivia_api.demo.model.TriviaModel;
import com.trivia_api.demo.model.QuestionModel;

import java.util.List;

public class QuestionConvert {
    public static QuestionModel convert(QuestionResponse questionResponse) {
        QuestionModel model = new QuestionModel();
        model.setResponse_code(questionResponse.response_code());

        // Converte os response em entidade
        List<TriviaModel> questions = questionResponse.results().stream()
                .map(q -> new TriviaModel(
                        null, // id gerado automaticamente
                        q.category(),
                        q.type(),
                        q.difficulty(),
                        q.question(),
                        q.correct_answer(),
                        q.incorrect_answers()
                ))
                .toList();

        model.setResults(questions);
        return model;
    }
}
