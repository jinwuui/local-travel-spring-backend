package com.jinwuui.howdoilook.dto.request;

import com.jinwuui.howdoilook.dto.validation.MaxFileCount;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
@Setter
@Builder
public class PlaceCreateRequest {

    @NotBlank(message = "내용을 입력해주세요.")
    private String name;

    @NotBlank(message = "내용을 입력해주세요.")
    private String description;

    @NotNull(message = "위치를 입력해주세요.")
    private Double lat;

    @NotNull(message = "위치를 입력해주세요.")
    private Double lng;

    private Long rating;

    private List<String> categories;

    @MaxFileCount(max = 10, message = "최대 10개의 사진을 업로드할 수 있습니다.")
    private List<MultipartFile> images;

}
