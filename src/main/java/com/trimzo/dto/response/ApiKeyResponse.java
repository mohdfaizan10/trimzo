package com.trimzo.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class ApiKeyResponse {

    private Long id;
    private String name;
    private Boolean isActive;
    private LocalDateTime lastUsed;
    private LocalDateTime createdAt;
}