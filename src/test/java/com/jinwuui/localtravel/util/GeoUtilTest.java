package com.jinwuui.localtravel.util;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class GeoUtilTest {

    @Autowired
    private TestGeoUtil geoUtil;

    @AfterEach
    void clean() {
        geoUtil.clearMockResponses();
    }

    @Test
    @DisplayName("국가 이름 조회")
    void getCountryName() throws IOException {
        // given
        double lat = 37.5665;
        double lng = 126.978;
        geoUtil.setMockResponse(lat, lng, "대한민국");

        // when
        String countryName = geoUtil.getCountryName(lat, lng);

        // then
        assertEquals("대한민국", countryName);
    }

    @Test
    @DisplayName("국가 이름 조회 - 독도")
    void getCountryNameDokdo() throws IOException {
        // given
        double lat = 37.239853;
        double lng = 131.869382;
        geoUtil.setMockResponse(lat, lng, "대한민국");
        
        // when
        String countryName = geoUtil.getCountryName(lat, lng);

        // then
        assertEquals("대한민국", countryName);
    }
}
