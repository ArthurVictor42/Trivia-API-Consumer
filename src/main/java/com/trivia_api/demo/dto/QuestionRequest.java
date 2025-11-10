package com.trivia_api.demo.dto;

import java.util.List;

public record QuestionRequest(int response_code,
                              List<TriviaResquest> results) {
}
