package com.trimzo.service;

import com.trimzo.dto.request.CreateUrlRequest;
import com.trimzo.dto.response.UrlResponse;
import com.trimzo.entity.Url;
import com.trimzo.entity.User;
import com.trimzo.exception.UrlExpiredException;
import com.trimzo.exception.UrlInactiveException;
import com.trimzo.exception.UrlNotFoundException;
import com.trimzo.repository.UrlRepository;
import com.trimzo.repository.UserRepository;
import com.trimzo.util.Base62Encoder;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UrlService {

    private final UrlRepository urlRepository;
    private final UserRepository userRepository;
    private final Base62Encoder base62Encoder;

    // application.properties se base URL lo
    @Value("${app.base-url}")
    private String baseUrl;

    /**
     * Long URL ko short karo!
     * Yeh project ka CORE feature hai!
     */
    @Transactional
    public UrlResponse shortenUrl(CreateUrlRequest request) {

        // 1. Current logged in user nikalo
        String email = SecurityContextHolder.getContext()
                .getAuthentication().getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException("User not found"));

        // 2. Same user ne yeh URL pehle add ki hai?
        var existing = urlRepository
                .findByOriginalUrlAndUser(
                        request.getOriginalUrl(), user);

        if (existing.isPresent()) {
            return buildResponse(existing.get());
        }

        // 3. Nayi URL entity banao
        Url url = new Url();
        url.setOriginalUrl(request.getOriginalUrl());
        url.setUser(user);
        url.setTitle(request.getTitle());
        url.setExpiresAt(request.getExpiresAt());

        // 4. Pehle temporary short code set karo
        //    (DB mein NOT NULL constraint hai)


        // 5. Save karo aur turant flush karo — ID generate hogi
        Url savedUrl = urlRepository.saveAndFlush(url);

        // 6. ID ko Base62 encode karo → Real short code!
        String shortCode = base62Encoder.encode(savedUrl.getId());

        // 7. Real short code update karo
        savedUrl.setShortCode(shortCode);
        urlRepository.save(savedUrl);

        // 8. Response return karo
        return buildResponse(savedUrl);
    }

    /**
     * User ki saari URLs fetch karo
     */
    public List<UrlResponse> getAllUrls() {

        String email = SecurityContextHolder.getContext()
                .getAuthentication().getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException("User not found"));

        return urlRepository.findByUserOrderByCreatedAtDesc(user)
                .stream()
                .map(this::buildResponse)
                .collect(Collectors.toList());
    }

    /**
     * Short code se original URL nikalo — Redirect ke liye
     */
    public String getOriginalUrl(String shortCode) {

        Url url = urlRepository.findByShortCode(shortCode)
                .orElseThrow(() -> new UrlNotFoundException(shortCode));

        // Expired hai?
        if (url.getExpiresAt() != null &&
                url.getExpiresAt()
                        .isBefore(java.time.LocalDateTime.now())) {
            throw new UrlExpiredException(shortCode);
        }

        // Active hai?
        if (!url.getIsActive()) {
            throw new UrlInactiveException(shortCode);
        }

        return url.getOriginalUrl();
    }

    /**
     * URL delete karo
     */
    @Transactional
    public void deleteUrl(String shortCode) {

        String email = SecurityContextHolder.getContext()
                .getAuthentication().getName();

        Url url = urlRepository.findByShortCode(shortCode)
                .orElseThrow(() ->
                        new RuntimeException("URL not found"));

        // Sirf owner delete kar sakta hai!
        if (!url.getUser().getEmail().equals(email)) {
            throw new RuntimeException(
                    "You are not authorized to delete this URL");
        }

        urlRepository.delete(url);
    }

    /**
     * Url entity ko UrlResponse mein convert karo
     */
    private UrlResponse buildResponse(Url url) {
        UrlResponse response = new UrlResponse();
        response.setId(url.getId());
        response.setShortCode(url.getShortCode());
        response.setShortUrl(baseUrl + "/" + url.getShortCode());
        response.setOriginalUrl(url.getOriginalUrl());
        response.setTitle(url.getTitle());
        response.setIsActive(url.getIsActive());
        response.setCreatedAt(url.getCreatedAt());
        response.setExpiresAt(url.getExpiresAt());
        return response;
    }
}