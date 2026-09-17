package com.trimzo.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ApiKeyGeneratedResponse {

    private String apiKey;
    private String name;
    private String message;
}