package com.jinwuui.howdoilook.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class EmbeddingUtil extends ApiRequestUtil {
    
    @Value("${key.api.openai}")
    private String openaiApiKey;

    @Value("${openai.embedding.api.url}")
    private String openaiEmbeddingApiUrl;

    @Value("${openai.embedding.model}")
    private String embeddingModel;

    public EmbeddingUtil(RedisTemplate<String, String> redisTemplate, RestTemplate restTemplate, ObjectMapper objectMapper) {
        super(redisTemplate, restTemplate, objectMapper);
    }

    public List<Double> fetchEmbedding(String text) {
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("input", text);
        requestBody.put("model", embeddingModel);

        JsonNode response = executeRequest(openaiEmbeddingApiUrl, openaiApiKey, requestBody, HttpMethod.POST, AuthType.BEARER, null);
        return extractEmbedding(response);
    }

    private List<Double> extractEmbedding(JsonNode response) {
        JsonNode embeddingNode = response.get("data")
                .get(0)
                .get("embedding");
        return objectMapper.convertValue(embeddingNode, new TypeReference<List<Double>>() {});
    }
}
