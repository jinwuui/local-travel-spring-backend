package com.jinwuui.howdoilook.repository.s3;

import io.awspring.cloud.s3.ObjectMetadata;
import io.awspring.cloud.s3.S3Resource;
import io.awspring.cloud.s3.S3Template;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Profile("prod")
@Repository
@RequiredArgsConstructor
public class AwsS3Repository extends AbstractS3Repository {

    private final S3Template s3Template;

    private final static String BUCKET_NAME = "how-do-i-look";

    @Override
    public String save(final MultipartFile file) throws IOException {
        if (!isImageFile(file.getOriginalFilename())) {
            throw new IllegalArgumentException("이미지 파일만 업로드할 수 있습니다.");
        }

        String key = UUID.randomUUID().toString();

        S3Resource s3Resource = s3Template.upload(BUCKET_NAME, key, file.getInputStream());
        return s3Resource.getURL().getPath();
    }

    @Override
    public long count() {
        return 0;
    }
}
