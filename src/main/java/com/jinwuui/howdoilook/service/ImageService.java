package com.jinwuui.howdoilook.service;

import com.jinwuui.howdoilook.domain.Feed;
import com.jinwuui.howdoilook.domain.Image;
import com.jinwuui.howdoilook.exception.InvalidFileFormatException;
import com.jinwuui.howdoilook.repository.ImageRepository;
import com.jinwuui.howdoilook.repository.S3Repository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ImageService {

    private final ImageRepository imageRepository;

    private final S3Repository s3Repository;

    private final ExecutorService executorService = Executors.newFixedThreadPool(10);

    public void saveImages(Feed feed, List<MultipartFile> images) {
        List<String> urls = saveImagesToS3(images).join();

        urls.forEach(url -> {
            if (!url.startsWith("failed: ")) {
                Image image = Image.builder()
                        .url(url)
                        .feed(feed)
                        .build();

                imageRepository.save(image);
            }
        });
    }

    private CompletableFuture<List<String>> saveImagesToS3(List<MultipartFile> images) {
        List<CompletableFuture<String>> futures = images.stream()
                .map(file -> CompletableFuture.supplyAsync(() -> {
                    try {
                        return s3Repository.save(file);
                    } catch (InvalidFileFormatException e) {
                        log.error("잘못된 파일 형식, 이미지 업로드 실패: {}", file.getOriginalFilename(), e);
                        return "failed: " + file.getOriginalFilename();
                    } catch (IOException e) {
                        log.error("IO 예외, 이미지 업로드 실패: {}", file.getOriginalFilename(), e);
                        return "failed: " + file.getOriginalFilename();
                    }
                }, executorService))
                .toList();

        return CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]))
                .thenApply(v -> futures.stream()
                        .map(CompletableFuture::join)
                        .toList());
    }
}
