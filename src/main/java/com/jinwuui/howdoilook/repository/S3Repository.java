package com.jinwuui.howdoilook.repository;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface S3Repository {

    String save(final MultipartFile file) throws IOException;

    long count();
}