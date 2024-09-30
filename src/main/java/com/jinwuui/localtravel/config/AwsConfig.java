package com.jinwuui.localtravel.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;

@Configuration
public class AwsConfig {

    @Value("${spring.cloud.aws.region.static}")
    private String region;

    @Bean
    public S3Client s3Client() {
        return S3Client.builder()
                .region(Region.of(region)) // 리전 설정
                .build();
    }

    // @Bean
    // public S3Template s3Template(S3Client s3Client) {
    // return new S3Template(s3Client);
    // }
}
