package com.jinwuui.localtravel.repository.s3;

import com.jinwuui.localtravel.repository.S3Repository;

import java.net.URLConnection;
import java.util.Objects;

public abstract class AbstractS3Repository implements S3Repository {

    private static final String IMAGE_CONTENT_TYPE_PREFIX = "image";

    protected boolean isImageFile(final String filename) {
        String contentType = guessContentType(filename);
        return isValidImageContentType(contentType);
    }

    private String guessContentType(final String filename) {
        return URLConnection.guessContentTypeFromName(filename);
    }

    private boolean isValidImageContentType(final String contentType) {
        return Objects.nonNull(contentType) && contentType.startsWith(IMAGE_CONTENT_TYPE_PREFIX);
    }
}

