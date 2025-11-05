package com.trivia_api.demo.client;

import com.trivia_api.demo.dto.QuestionResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "triviaClient", url = "https://opentdb.com")
public interface TriviaClient {
    @GetMapping("/api.php")
    QuestionResponse getTrivia(
            @RequestParam("amount") int amount);
}
