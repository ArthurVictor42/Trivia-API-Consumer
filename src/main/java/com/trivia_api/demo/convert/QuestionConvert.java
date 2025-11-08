package com.trivia_api.demo.convert;

import com.trivia_api.demo.dto.QuestionResponse;
import com.trivia_api.demo.model.QuestionModel;
import com.trivia_api.demo.model.TriviaModel;

import java.util.List;

public class QuestionConvert {
    public static TriviaModel convert(QuestionResponse questionResponse) {
        TriviaModel model = new TriviaModel();
        model.setResponse_code(questionResponse.response_code());

        // Converte os response em entidade
        List<QuestionModel> questions = questionResponse.results().stream()
                .map(q -> new QuestionModel(
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
