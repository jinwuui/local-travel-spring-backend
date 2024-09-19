package com.jinwuui.howdoilook.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;

@Component
@Profile("prod")
@RequiredArgsConstructor
public class GeoUtil {

    private static final String GEOCODE_API_URL = "https://maps.googleapis.com/maps/api/geocode/json";
    private static final String REGION = "KR";
    private static final String LANGUAGE = "ko";
    private static final String RESULT_TYPE = "country";

    @Value("${key.api.google}")
    private String googleApiKey;

    private final RestTemplate restTemplate;

    private final ObjectMapper objectMapper;

    public String getCountryName(double lat, double lng) throws IOException {
        if (isDokdo(lat, lng)) return "대한민국";

        String url = buildGeocodeUrl(lat, lng);
        String response = restTemplate.getForObject(url, String.class);
        return extractCountryName(response);
    }

    private String buildGeocodeUrl(double lat, double lng) {
        return UriComponentsBuilder.fromHttpUrl(GEOCODE_API_URL)
                .queryParam("key", this.googleApiKey)
                .queryParam("region", REGION)
                .queryParam("latlng", lat + "," + lng)
                .queryParam("result_type", RESULT_TYPE)
                .queryParam("language", LANGUAGE)
                .toUriString();
    }

    private String extractCountryName(String response) throws IOException {
        JsonNode root = objectMapper.readTree(response);
        JsonNode results = root.path("results");

        if (results.size() > 0) {
            JsonNode addressComponents = results.get(0).path("address_components");
            for (JsonNode component : addressComponents) {
                JsonNode types = component.path("types");
                if (types.toString().contains("country")) {
                    return component.path("long_name").asText();
                }
            }
        }

        // NOTE: 나라 이름이 없는 위치 정보는 null로 반환
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
            return lat >= LAT_MIN && lat <= LAT_MAX &&
                   lng >= LNG_MIN && lng <= LNG_MAX;
        }
    }
}
