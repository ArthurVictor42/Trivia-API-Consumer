package com.trivia_api.demo.Service;

import com.trivia_api.demo.Repository.QuestionRepository;
import com.trivia_api.demo.dto.QuestionResResponse;
import com.trivia_api.demo.dto.QuestionResponse;
import com.trivia_api.demo.model.Question;
import com.trivia_api.demo.model.QuestionModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class QuestionService {

    @Autowired
    private QuestionRepository questionRepository;

    public void inserir(QuestionResponse questionResponse) {
        QuestionModel model = new QuestionModel();
        model.setResponse_code(questionResponse.response_code());

        List<Question> questions = questionResponse.results().stream()
                .map(q -> new Question(
                        null,
                        q.category(),
                        q.type(),
                        q.difficulty(),
                        q.question(),
                        q.correct_answer(),
                        q.incorrect_answers()
                ))
                .toList();

        model.setResults(questions);

        questionRepository.save(model);
    }

    public QuestionResponse buscarId(long id) {
        return questionRepository.findById(id)
                .map(model -> new QuestionResponse(
                        model.getResponse_code(),
                        model.getResults().stream()
                                .map(q -> new QuestionResResponse(
                                        q.getCategory(),
                                        q.getType(),
                                        q.getDifficulty(),
                                        q.getQuestion(),
                                        q.getCorrect_answer(),
                                        q.getIncorrect_answers()
                                ))
                                .toList()
                ))
                .orElseThrow(() -> new RuntimeException("QuestionModel com ID " + id + " não encontrado."));
    }

    public List<QuestionResponse> buscarTodos() {
        return questionRepository.findAll()
                .stream()
                .map(model -> new QuestionResponse(
                        model.getResponse_code(),
                        model.getResults().stream()
                                .map(q -> new QuestionResResponse(
                                        q.getCategory(),
                                        q.getType(),
                                        q.getDifficulty(),
                                        q.getQuestion(),
                                        q.getCorrect_answer(),
                                        q.getIncorrect_answers()
                                ))
                                .toList()
                ))
                .toList();
    }

    public void atualizarQuestao(long id, QuestionResponse questionRequest) {
        QuestionModel model = questionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("QuestionModel com ID " + id + " não encontrado."));

        model.setResponse_code(questionRequest.response_code());

        // Evita trocar a referência da lista
        model.getResults().clear();

        questionRequest.results().forEach(q -> {
            Question question = new Question();
            question.setCategory(q.category());
            question.setType(q.type());
            question.setDifficulty(q.difficulty());
            question.setQuestion(q.question());
            question.setCorrect_answer(q.correct_answer());
            question.setIncorrect_answers(new ArrayList<>(q.incorrect_answers()));

            model.getResults().add(question);
        });

        questionRepository.save(model);
    }

    public void deletarQuestao(long id) {
        QuestionModel model = questionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("QuestionModel com ID " + id + " não encontrado."));

        questionRepository.delete(model);
    }

}
