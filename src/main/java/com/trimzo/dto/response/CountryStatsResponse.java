package com.trimzo.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class CountryStatsResponse {
    private String country;
    private String city;
    private Long clicks;
}