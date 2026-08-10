package com.trimzo.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ReferrerStatsResponse {
    private String source;
    private Long clicks;
}