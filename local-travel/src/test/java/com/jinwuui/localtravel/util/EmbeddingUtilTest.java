package com.jinwuui.localtravel.util;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBootTest
public class EmbeddingUtilTest {

    @Autowired
    private EmbeddingUtil embeddingUtil;

    @Test
    @DisplayName("임베딩 생성")
    void createEmbedding() {
        // given
        String text = "정말 좋아요";

        // when
        List<Double> embedding = embeddingUtil.fetchEmbedding(text);

        // then
        assertNotNull(embedding);
    }

}
