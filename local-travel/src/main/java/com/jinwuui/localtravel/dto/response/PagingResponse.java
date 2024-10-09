package com.jinwuui.localtravel.dto.response;

import java.util.List;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PagingResponse<T> {

    private final long size;

    private final List<T> items;

}
