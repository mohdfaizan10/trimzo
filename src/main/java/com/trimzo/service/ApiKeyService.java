package com.trimzo.service;

import com.trimzo.dto.request.GenerateKeyRequest;
import com.trimzo.dto.response.ApiKeyGeneratedResponse;
import com.trimzo.dto.response.ApiKeyResponse;
import com.trimzo.entity.ApiKey;
import com.trimzo.entity.User;
import com.trimzo.repository.ApiKeyRepository;
import com.trimzo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ApiKeyService {

    private final ApiKeyRepository apiKeyRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * Generates a new API key for the authenticated user.
     * Raw key is returned ONCE and never stored in plain text.
     */
    @Transactional
    public ApiKeyGeneratedResponse generateKey(
            GenerateKeyRequest request) {

        User user = getCurrentUser();

        // Generate 64-character raw key
        String rawKey = UUID.randomUUID()
                .toString().replace("-", "")
                + UUID.randomUUID()
                .toString().replace("-", "");

        // Store BCrypt hash — never the raw key
        ApiKey apiKey = new ApiKey();
        apiKey.setUser(user);
        apiKey.setKeyValue(passwordEncoder.encode(rawKey));
        apiKey.setName(request.getName());
        apiKeyRepository.save(apiKey);

        return new ApiKeyGeneratedResponse(
                rawKey,
                request.getName(),
                "Save this key securely — it will not be shown again!"
        );
    }

    /**
     * Returns all active API keys for the authenticated user.
     * Raw key values are never returned.
     */
    public List<ApiKeyResponse> getMyKeys() {
        User user = getCurrentUser();

        return apiKeyRepository
                .findByUserAndIsActiveTrue(user)
                .stream()
                .map(key -> new ApiKeyResponse(
                        key.getId(),
                        key.getName(),
                        key.getIsActive(),
                        key.getLastUsed(),
                        key.getCreatedAt()
                ))
                .collect(Collectors.toList());
    }

    /**
     * Revokes an API key by setting it to inactive.
     * Only the key owner can revoke it.
     */
    @Transactional
    public void revokeKey(Long keyId) {
        User user = getCurrentUser();

        ApiKey apiKey = apiKeyRepository.findById(keyId)
                .orElseThrow(() ->
                        new RuntimeException("API key not found"));

        if (!apiKey.getUser().getId().equals(user.getId())) {
            throw new RuntimeException( "Access denied: you do not own this key");

        }

        apiKey.setIsActive(false);
        apiKeyRepository.save(apiKey);
    }

    private User getCurrentUser() {
        String email = SecurityContextHolder.getContext()
                .getAuthentication().getName();

        return userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException("User not found"));
    }
}