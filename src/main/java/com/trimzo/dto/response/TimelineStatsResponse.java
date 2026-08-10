package com.trimzo.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class TimelineStatsResponse {
    private String date;
    private Long clicks;
}