package com.trimzo.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GenerateKeyRequest {

    @NotBlank(message = "Key name is required")
    private String name;
}