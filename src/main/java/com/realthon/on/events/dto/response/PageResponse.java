package com.realthon.on.events.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PageResponse<T> {
    private List<T> content;     // 실제 데이터
    private int page;            // 현재 페이지 번호
    private int size;            // 페이지 크기
    private long totalElements;  // 전체 개수
    private int totalPages;      // 전체 페이지 수
}
