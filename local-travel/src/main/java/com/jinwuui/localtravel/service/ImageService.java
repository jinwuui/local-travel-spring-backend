package com.jinwuui.localtravel.service;

import com.jinwuui.localtravel.domain.Place;
import com.jinwuui.localtravel.domain.Image;
import com.jinwuui.localtravel.exception.InvalidFileFormatException;
import com.jinwuui.localtravel.repository.ImageRepository;
import com.jinwuui.localtravel.repository.S3Repository;
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

    public void saveImages(Place place, List<MultipartFile> images) {
        List<String> urls = saveImagesToS3(images).join();

        urls.forEach(url -> {
            if (!url.startsWith("failed: ")) {
                Image image = Image.builder()
                        .url(url)
                        .place(place)
                        .build();

                imageRepository.save(image);
            }
        });
    }

    private CompletableFuture<List<String>> saveImagesToS3(List<MultipartFile> images) {
        List<CompletableFuture<String>> futures = images.stream()
                .map(file -> CompletableFuture.supplyAsync(() -> {
                    try {
                        String filename = s3Repository.save(file);
                        return filename;
                    } catch (InvalidFileFormatException e) {
                        log.error("잘못된 파일 형식, 이미지 업로드 실패: {}", file.getOriginalFilename(), e);
                        return "failed: " + file.getOriginalFilename();
                    } catch (IOException e) {
                        log.error("IO 예외, 이미지 업로드 실패: {}", file.getOriginalFilename(), e);
                        return "failed: " + file.getOriginalFilename();
                    } catch (Exception e) {
                        log.error("예외 발생, 이미지 업로드 실패: {}", file.getOriginalFilename(), e);
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
