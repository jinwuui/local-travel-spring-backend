package com.jinwuui.howdoilook.util;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class EmbeddingUtil {
    
    private static final String CACHE_KEY_PREFIX = "embedding:";
    private static final String CACHE_HIT_MESSAGE = "[ Cache Hit : text-embedding ]";
    private static final String CACHE_MISS_MESSAGE = "[ Cache Miss: text-embedding ]";

    @Value("${key.api.openai}")
    private String openaiApiKey;

    @Value("${openai.embedding.api.url}")
    private String openaiEmbeddingApiUrl;

    @Value("${openai.embedding.model}")
    private String embeddingModel;

    @Value("${spring.data.redis.cache.expiration}")
    private long cacheExpiration;

    private final RedisTemplate<String, String> redisTemplate;

    private final RestTemplate restTemplate;
    
    private final ObjectMapper objectMapper;

    public List<Double> fetchEmbedding(String text) {
        String cacheKey = CACHE_KEY_PREFIX + text;

        String cachedEmbedding = redisTemplate.opsForValue().get(cacheKey);
        if (cachedEmbedding != null) {
            log.info(">>> {}", CACHE_HIT_MESSAGE);
            return deserializeEmbedding(cachedEmbedding);
        }

        log.info(">>> {}", CACHE_MISS_MESSAGE);
        List<Double> embedding = callOpenAiApi(text);
        cacheEmbedding(cacheKey, embedding);
        return embedding;
    }

    private List<Double> deserializeEmbedding(String cachedEmbedding) {
        try {
            return objectMapper.readValue(cachedEmbedding, new TypeReference<List<Double>>() {});
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to deserialize cached embedding", e);
        }
    }

    private List<Double> callOpenAiApi(String text) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("Authorization", "Bearer " + openaiApiKey);

            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("input", text);
            requestBody.put("model", embeddingModel);

            HttpEntity<Map<String, Object>> request = new HttpEntity<>(requestBody, headers);
            ResponseEntity<String> responseEntity = restTemplate.postForEntity(openaiEmbeddingApiUrl, request, String.class);
            JsonNode response = objectMapper.readTree(responseEntity.getBody());
            return extractEmbeddingFromResponse(response);
        } catch (IOException e) {
            throw new RuntimeException("Failed to fetch embedding from OpenAI", e);
        }
    }

    private List<Double> extractEmbeddingFromResponse(JsonNode response) {
        return objectMapper.convertValue(response.get("data").get(0).get("embedding"), new TypeReference<List<Double>>() {});
    }

    private void cacheEmbedding(String cacheKey, List<Double> embedding) {
        try {
            String serializedEmbedding = objectMapper.writeValueAsString(embedding);
            redisTemplate.opsForValue().set(cacheKey, serializedEmbedding, cacheExpiration, TimeUnit.SECONDS);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to serialize embedding for caching", e);
        }
    }
}
