package com.trivia_api.demo.Repository;

import com.trivia_api.demo.model.TriviaModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QuestionRepository extends JpaRepository<TriviaModel, Long> {
}
