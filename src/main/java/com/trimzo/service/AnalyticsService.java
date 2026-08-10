package com.trimzo.service;

import com.trimzo.dto.response.*;
import com.trimzo.entity.Url;
import com.trimzo.repository.ClickRepository;
import com.trimzo.repository.UrlRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AnalyticsService {

    private final ClickRepository clickRepository;
    private final UrlRepository urlRepository;

    private Url getUrlAndVerifyOwnership(String shortCode) {
        String email = SecurityContextHolder.getContext()
                .getAuthentication().getName();

        Url url = urlRepository.findByShortCode(shortCode)
                .orElseThrow(() -> new RuntimeException(
                        "URL not found: " + shortCode));

        if (!url.getUser().getEmail().equals(email)) {
            throw new RuntimeException(
                    "Access denied: you do not own this URL");
        }

        return url;
    }

    public AnalyticsSummaryResponse getSummary(String shortCode) {
        Url url = getUrlAndVerifyOwnership(shortCode);

        Long total = clickRepository.countByUrlId(url.getId());
        Long unique = clickRepository.countUniqueByUrlId(url.getId());

        LocalDateTime lastClicked = clickRepository
                .findTopByUrlIdOrderByClickedAtDesc(url.getId())
                .map(c -> c.getClickedAt())
                .orElse(null);

        return new AnalyticsSummaryResponse(
                url.getShortCode(),
                url.getOriginalUrl(),
                total,
                unique,
                lastClicked,
                url.getCreatedAt()
        );
    }

    public List<CountryStatsResponse> getCountryStats(String shortCode) {
        Url url = getUrlAndVerifyOwnership(shortCode);

        return clickRepository.getCountryStats(url.getId())
                .stream()
                .map(row -> new CountryStatsResponse(
                        (String) row[0],
                        (String) row[1],
                        (Long) row[2]
                ))
                .collect(Collectors.toList());
    }

    public List<DeviceStatsResponse> getDeviceStats(String shortCode) {
        Url url = getUrlAndVerifyOwnership(shortCode);

        return clickRepository.getDeviceStats(url.getId())
                .stream()
                .map(row -> new DeviceStatsResponse(
                        (String) row[0],
                        (Long) row[1]
                ))
                .collect(Collectors.toList());
    }

    public List<ReferrerStatsResponse> getReferrerStats(String shortCode) {
        Url url = getUrlAndVerifyOwnership(shortCode);

        return clickRepository.getReferrerStats(url.getId())
                .stream()
                .map(row -> new ReferrerStatsResponse(
                        (String) row[0],
                        (Long) row[1]
                ))
                .collect(Collectors.toList());
    }

    public List<TimelineStatsResponse> getTimeline(
            String shortCode,
            LocalDateTime from,
            LocalDateTime to) {

        Url url = getUrlAndVerifyOwnership(shortCode);

        LocalDateTime fromDate = (from != null) ? from
                : LocalDateTime.now().minusDays(30);
        LocalDateTime toDate = (to != null) ? to
                : LocalDateTime.now();

        return clickRepository.getTimeline(url.getId(), fromDate, toDate)
                .stream()
                .map(row -> new TimelineStatsResponse(
                        row[0].toString(),
                        (Long) row[1]
                ))
                .collect(Collectors.toList());
    }
}