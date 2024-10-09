package com.jinwuui.localtravel.repository.s3;

import io.awspring.cloud.s3.S3Resource;
import io.awspring.cloud.s3.S3Template;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;
import org.springframework.web.multipart.MultipartFile;
import com.jinwuui.localtravel.exception.InvalidFileFormatException;

import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Profile({"dev", "prod"})
@Repository
@RequiredArgsConstructor
public class AwsS3Repository extends AbstractS3Repository {

    private static final String IMAGE_PATH = "images/originals/";

    @Value("${spring.cloud.aws.s3.bucket.name}")
    private String bucketName;

    private final S3Template s3Template;

    @Override
    public String save(final MultipartFile file) throws IOException {
        validateFile(file);
        String key = generateS3Key(file);
        S3Resource s3Resource = s3Template.upload(bucketName, key, file.getInputStream());
        return s3Resource.getURL().getPath();
    }

    private void validateFile(MultipartFile file) {
        if (!isImageFile(file.getOriginalFilename())) {
            throw new InvalidFileFormatException(file.getOriginalFilename());
        }
    }

    private String generateS3Key(MultipartFile file) {
        String extension = getFileExtension(file.getOriginalFilename())
                .orElseThrow(() -> new InvalidFileFormatException(file.getOriginalFilename()));
        return IMAGE_PATH + UUID.randomUUID() + "." + extension;
    }

    private Optional<String> getFileExtension(String filename) {
        int lastIndex = filename.lastIndexOf('.');
        return (lastIndex > 0) ? Optional.of(filename.substring(lastIndex + 1)) : Optional.empty();
    }
}
