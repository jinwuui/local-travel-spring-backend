package com.jinwuui.localtravel.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Map;
import java.util.Optional;

import static org.springframework.http.HttpMethod.*;

@Slf4j
@RequiredArgsConstructor
public abstract class ApiRequestUtil {

    protected final RedisTemplate<String, String> redisTemplate;

    protected final RestTemplate restTemplate;

    protected final ObjectMapper objectMapper;

    protected enum AuthType {
        NONE, BEARER, QUERY_PARAM, HEADER
    }

    protected JsonNode getRequest(String url, String apiKey, AuthType authType, String authParamName) {
        return executeRequest(url, apiKey, null, GET, authType, authParamName);
    }

    protected JsonNode postRequest(String url, String apiKey, Map<String, Object> requestBody, AuthType authType, String authParamName) {
        return executeRequest(url, apiKey, requestBody, POST, authType, authParamName);
    }

    protected JsonNode executeRequest(String url, String apiKey, Map<String, Object> requestBody, HttpMethod method, AuthType authType, String authParamName) {
        String cacheKey = generateCacheKey(url, requestBody);

        return getCachedResponse(cacheKey)
                .orElseGet(() -> {
                    JsonNode response = sendApiRequest(url, apiKey, requestBody, method, authType, authParamName);
                    cacheResponse(cacheKey, response);
                    return response;
                });
    }

    private String generateCacheKey(String url, Map<String, Object> requestBody) {
        StringBuilder keyBuilder = new StringBuilder(url);
        
        if (requestBody != null) {
            requestBody.entrySet().stream()
                    .sorted(Map.Entry.comparingByKey())
                    .forEach(entry -> keyBuilder.append("_")
                    .append(entry.getKey()).append("_").append(entry.getValue()));
        }

        return keyBuilder.toString();
    }

    private Optional<JsonNode> getCachedResponse(String cacheKey) {
        try {
            String cachedResponse = redisTemplate.opsForValue().get(cacheKey);
            return Optional.ofNullable(cachedResponse)
                    .map(this::parseJsonNode);
        } catch (Exception e) {
            log.error("캐시 확인 중 오류 발생", e);
            return Optional.empty();
        }
    }

    private JsonNode sendApiRequest(String url, String apiKey, Map<String, Object> requestBody, HttpMethod method, AuthType authType, String authParamName) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url);

        switch (authType) {
            case BEARER:
                headers.set("Authorization", "Bearer " + apiKey);
                break;
            case QUERY_PARAM:
                builder.queryParam(authParamName, apiKey);
                break;
            case HEADER:
                headers.set(authParamName, apiKey);
                break;
            case NONE:
                break;
        }

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(requestBody, headers);
        ResponseEntity<String> responseEntity = restTemplate.exchange(builder.toUriString(), method, request, String.class);
        return parseJsonNode(responseEntity.getBody());
    }

    private void cacheResponse(String cacheKey, JsonNode response) {
        try {
            String serializedResponse = objectMapper.writeValueAsString(response);
            redisTemplate.opsForValue().set(cacheKey, serializedResponse);
        } catch (JsonProcessingException e) {
            log.error("응답 캐싱 중 오류 발생", e);
        }
    }

    private JsonNode parseJsonNode(String json) {
        try {
            return objectMapper.readTree(json);
        } catch (JsonProcessingException e) {
            log.error("JSON 파싱 중 오류 발생", e);
            throw new RuntimeException("JSON 파싱 실패", e);
        }
    }
}
