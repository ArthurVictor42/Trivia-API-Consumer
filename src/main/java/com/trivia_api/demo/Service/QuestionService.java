package com.trivia_api.demo.Service;

import com.trivia_api.demo.Repository.QuestionRepository;
import com.trivia_api.demo.dto.TriviaResponse;
import com.trivia_api.demo.dto.QuestionResponse;
import com.trivia_api.demo.model.TriviaModel;
import com.trivia_api.demo.model.QuestionModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class QuestionService {

    @Autowired
    private QuestionRepository questionRepository;


    // Metodos paras os bloco

    // Salva um novo conjunto de perguntas no banco
    public void inserir(QuestionResponse questionResponse) {
        QuestionModel model = new QuestionModel();
        model.setResponse_code(questionResponse.response_code());

        List<TriviaModel> questions = questionResponse.results().stream()
                .map(q -> new TriviaModel(
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
                .orElseThrow(() -> new RuntimeException("Questão  não encontrada."));
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
        QuestionModel model = questionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Questão não encontrada."));

        model.setResponse_code(questionRequest.response_code());

        // Limpa as perguntas antigas e adiciona as novas
        model.getResults().clear();

        questionRequest.results().forEach(q -> {
            TriviaModel question = new TriviaModel();
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
        QuestionModel model = questionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Questão não encontrada."));

        questionRepository.delete(model);
    }

    //Metodos das questões individuais

    public TriviaModel buscarQuestaodobloco(long id, long questionId) {
        // Buscar o bloco da questão
        QuestionModel bloco = questionRepository.findById(id).
                orElseThrow(() -> new RuntimeException("Bloco de Questão não encontrada"));

        // Buscar a questão
        return bloco.getResults().stream().
                filter(q -> q.getId().equals(questionId)).findFirst().
                orElseThrow(() -> new RuntimeException("Questão não encontrada nesse bloco"));
    }

    public void atualizarQuestaoid(long id, long idQuestion, TriviaResponse triviaResponse) {
        // Buscar o bloco da questão
        QuestionModel bloco = questionRepository.findById(id).
                orElseThrow(() -> new RuntimeException("Bloco de questão não encontrada"));

        // Buscar a questão
        TriviaModel question = bloco.getResults().stream()
                .filter(q -> q.getId().equals(idQuestion))
                .findFirst().orElseThrow(() -> new RuntimeException("Questão não encontrada"));

        //atualizar as informações
        question.setCategory(triviaResponse.category());
        question.setType(triviaResponse.type());
        question.setDifficulty(triviaResponse.difficulty());
        question.setQuestion(triviaResponse.question());
        question.setCorrect_answer(triviaResponse.correct_answer());
        question.setIncorrect_answers(triviaResponse.incorrect_answers());

        // Salva as informações novas no bloco
        questionRepository.save(bloco);

    }

    public void deletarquestoaid(long id, long questionId) {
        QuestionModel bloco = questionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Bloco não encontrado."));

        bloco.getResults().removeIf(q -> q.getId().equals(questionId));

        questionRepository.save(bloco);
    }
}
