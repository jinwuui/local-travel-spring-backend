package com.jinwuui.howdoilook.dto.service;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
@Setter
@Builder
public class FeedDto {

    private String content;

    private List<MultipartFile> images;

}
