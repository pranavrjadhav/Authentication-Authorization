package com.zenton.auth.Authorization.dtos.Admindtos;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PageResponse<T> {
    private List<T> content;

    private int currentPage;

    private int pageSize;

    private long totalElements;

    private int totalPages;

    private boolean first;

    private boolean last;
}
