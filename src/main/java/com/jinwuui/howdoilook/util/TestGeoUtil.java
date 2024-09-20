package com.jinwuui.howdoilook.util;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
@Profile("test")
public class TestGeoUtil extends GeoUtil {

    private Map<String, String> mockResponses = new HashMap<>();

    public TestGeoUtil() {
        super(null, null, null);
    }

    @Override
    public String getCountryName(double lat, double lng) {
        String key = lat + "," + lng;
        return mockResponses.getOrDefault(key, "Unknown");
    }

    public void setMockResponse(double lat, double lng, String countryName) {
        String key = lat + "," + lng;
        mockResponses.put(key, countryName);
    }

    public void clearMockResponses() {
        mockResponses.clear();
    }
}
