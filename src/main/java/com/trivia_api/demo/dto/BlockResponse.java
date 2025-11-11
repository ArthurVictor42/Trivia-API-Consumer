package com.trivia_api.demo.dto;


import java.util.List;

public record BlockResponse(int response_code,
                            List<TriviaResponse> results) {
}
