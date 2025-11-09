package com.trivia_api.demo.Service;

import com.trivia_api.demo.Repository.QuestionRepository;
import com.trivia_api.demo.dto.TriviaResponse;
import com.trivia_api.demo.dto.QuestionResponse;
import com.trivia_api.demo.model.QuestionModel;
import com.trivia_api.demo.model.TriviaModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class QuestionService {

    @Autowired
    private QuestionRepository questionRepository;

    // Salva um novo conjunto de perguntas no banco
    public void inserir(QuestionResponse questionResponse) {
        TriviaModel model = new TriviaModel();
        model.setResponse_code(questionResponse.response_code());

        List<QuestionModel> questions = questionResponse.results().stream()
                .map(q -> new QuestionModel(
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
    // Busca um registro pelo ID e converte de volta pra QuestionResponse
    public QuestionResponse buscarId(long id) {
        return questionRepository.findById(id)
                .map(model -> new QuestionResponse(
                        model.getResponse_code(),
                        model.getResults().stream()
                                .map(q -> new TriviaResponse(
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

    // Retorna todos os registros do banco convertidos em QuestionResponse
    public List<QuestionResponse> buscarTodos() {
        return questionRepository.findAll()
                .stream()
                .map(model -> new QuestionResponse(
                        model.getResponse_code(),
                        model.getResults().stream()
                                .map(q -> new TriviaResponse(
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
    // Atualiza um registro existente com novas perguntas
    public void atualizarQuestao(long id, QuestionResponse questionRequest) {
        TriviaModel model = questionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("QuestionModel com ID " + id + " não encontrado."));

        model.setResponse_code(questionRequest.response_code());

        // Limpa as perguntas antigas e adiciona as novas
        model.getResults().clear();

        questionRequest.results().forEach(q -> {
            QuestionModel question = new QuestionModel();
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
    // Deleta um registro do banco pelo ID
    public void deletarQuestao(long id) {
        TriviaModel model = questionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("QuestionModel com ID " + id + " não encontrado."));

        questionRepository.delete(model);
    }

}
