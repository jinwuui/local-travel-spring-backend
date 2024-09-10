package com.jinwuui.howdoilook.dto.request;

import com.jinwuui.howdoilook.dto.validation.MaxFileCount;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
@Setter
@Builder
public class FeedCreateRequest {

    @NotBlank(message = "내용을 입력해주세요.")
    private String content;

    @NotEmpty(message = "사진은 한 장 이상 필수입니다.")
    @MaxFileCount(max = 10, message = "최대 10개의 사진을 업로드할 수 있습니다.")
    private List<MultipartFile> images;

}
