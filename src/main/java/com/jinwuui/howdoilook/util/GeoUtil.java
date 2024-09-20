package com.jinwuui.howdoilook.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
@Profile({"dev","prod"})
public class GeoUtil extends ApiRequestUtil {

    @Value("${google.geocode.api.url}")
    private String geocodeApiUrl;

    @Value("${google.geocode.region}")
    private String region;

    @Value("${google.geocode.language}")
    private String language;

    @Value("${google.geocode.result-type}")
    private String resultType;

    @Value("${key.api.google}")
    private String googleApiKey;

    public GeoUtil(RedisTemplate<String, String> redisTemplate, RestTemplate restTemplate, ObjectMapper objectMapper) {
        super(redisTemplate, restTemplate, objectMapper);
    }

    public String getCountryName(double lat, double lng) {
        if (isDokdo(lat, lng)) return "대한민국";

        Map<String, String> queryParams = new HashMap<>();
        queryParams.put("latlng", lat + "," + lng);
        queryParams.put("result_type", resultType);
        queryParams.put("language", language);
        queryParams.put("region", region);

        String url = UriComponentsBuilder.fromHttpUrl(geocodeApiUrl)
                .queryParams(new LinkedMultiValueMap<>(queryParams.entrySet().stream()
                        .collect(java.util.stream.Collectors.toMap(
                                Map.Entry::getKey,
                                e -> java.util.Collections.singletonList(e.getValue())
                        ))))
                .build()
                .toUriString();

        JsonNode response = getRequest(url, googleApiKey, AuthType.QUERY_PARAM, "key");
        return extractCountryName(response);
    }

    private String extractCountryName(JsonNode response) {
        JsonNode results = response.path("results");

        if (results.size() > 0) {
            JsonNode addressComponents = results.get(0).path("address_components");
            for (JsonNode component : addressComponents) {
                JsonNode types = component.path("types");
                if (types.toString().contains("country")) {
                    return component.path("long_name").asText();
                }
            }
        }

        return null;
    }

    private static boolean isDokdo(double lat, double lng) {
        return DokdoBounds.isWithinBounds(lat, lng);
    }

    private static class DokdoBounds {
        private static final double LAT_MIN = 37.237146;
        private static final double LAT_MAX = 37.255864;
        private static final double LNG_MIN = 131.844482;
        private static final double LNG_MAX = 131.8782;

        static boolean isWithinBounds(double lat, double lng) {
            return lat >= LAT_MIN && lat <= LAT_MAX
                && lng >= LNG_MIN && lng <= LNG_MAX;
        }
    }
}
