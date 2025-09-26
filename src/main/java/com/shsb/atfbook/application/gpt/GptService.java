package com.shsb.atfbook.application.gpt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shsb.atfbook.domain.gpt.GptMessage;
import com.shsb.atfbook.domain.gpt.GptRequest;
import com.shsb.atfbook.domain.gpt.GptResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.List;

@Slf4j
@Service
public class GptService {
    @Qualifier("gptApi")
    @Autowired
    private RestClient gptApi;

    private ObjectMapper objectMapper = new ObjectMapper(); // JSON 변환기

    public GptResponse chatGpt(String msg) {
        GptRequest request = GptRequest.builder()
                .messages(List.of(
                        new GptMessage("user", msg)
                )).build();

        return gptApi.post()
                .uri("/v1/chat/completions")
                .body(request)
                .retrieve()
                .toEntity(GptResponse.class)
                .getBody();
    }
}
