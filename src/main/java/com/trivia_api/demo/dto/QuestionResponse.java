package com.trivia_api.demo.dto;


import java.util.List;

public record QuestionResponse(int response_code,
                               List<QuestionResResponse> results) {
}
