package com.trimzo.controller;

import com.trimzo.dto.request.GenerateKeyRequest;
import com.trimzo.dto.response.ApiKeyGeneratedResponse;
import com.trimzo.dto.response.ApiKeyResponse;
import com.trimzo.service.ApiKeyService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/api-keys")
@RequiredArgsConstructor
@Tag(name = "API Keys", description = "API Key management for third-party integration")
public class ApiKeyController {

    private final ApiKeyService apiKeyService;

    /**
     * Generate a new API key.
     * Raw key is returned ONCE — user must save it immediately.
     */
    @Operation(summary = "Generate a new API key")
    @PostMapping
    public ResponseEntity<ApiKeyGeneratedResponse> generateKey(
            @Valid @RequestBody GenerateKeyRequest request) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(apiKeyService.generateKey(request));
    }

    /**
     * Get all active API keys of the authenticated user.
     * Raw key values are never returned.
     */
    @Operation(summary = "Get all active API keys")
    @GetMapping
    public ResponseEntity<List<ApiKeyResponse>> getMyKeys() {

        return ResponseEntity.ok(apiKeyService.getMyKeys());
    }

    /**
     * Revoke an API key by ID.
     * Key is marked inactive — cannot be used after this.
     */
    @Operation(summary = "Revoke an API key")
    @DeleteMapping("/{keyId}")
    public ResponseEntity<String> revokeKey(
            @PathVariable Long keyId) {

        apiKeyService.revokeKey(keyId);
        return ResponseEntity.ok("API key revoked successfully");
    }
}