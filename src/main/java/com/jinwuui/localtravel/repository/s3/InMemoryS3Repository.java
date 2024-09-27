package com.jinwuui.localtravel.repository.s3;

import com.jinwuui.localtravel.exception.InvalidFileFormatException;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Profile({"dev", "test"})
@Repository
public class InMemoryS3Repository extends AbstractS3Repository {

    private final Map<String, byte[]> files = new HashMap<>();

    @Override
    public String save(final MultipartFile file) throws IOException {
        String filename = file.getOriginalFilename();

        if (filename == null || !isImageFile(filename)) {
            throw new InvalidFileFormatException(filename);
        }

        String key = UUID.randomUUID().toString();
        files.put(key, file.getBytes());

        return key;
    }

    @Override
    public long count() {
        return files.size();
    }
}
