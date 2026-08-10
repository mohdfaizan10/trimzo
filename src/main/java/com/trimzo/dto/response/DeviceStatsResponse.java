package com.trimzo.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class DeviceStatsResponse {
    private String deviceType;
    private Long clicks;
}