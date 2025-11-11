package com.trivia_api.demo.dto;

import java.util.List;

public record BlockRequest(int response_code,
                           List<TriviaResquest> results) {
}
