package com.trimzo.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class AnalyticsSummaryResponse {
    private String shortCode;
    private String originalUrl;
    private Long totalClicks;
    private Long uniqueClicks;
    private LocalDateTime lastClickedAt;
    private LocalDateTime createdAt;
}