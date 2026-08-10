package com.trimzo.controller;

import com.trimzo.dto.response.*;
import com.trimzo.service.AnalyticsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/v1/analytics")
@RequiredArgsConstructor
@Tag(name = "Analytics", description = "URL Click Analytics APIs")
public class AnalyticsController {

    private final AnalyticsService analyticsService;

    @Operation(summary = "Get total and unique click summary")
    @GetMapping("/{shortCode}/summary")
    public ResponseEntity<AnalyticsSummaryResponse> getSummary(
            @PathVariable String shortCode) {

        return ResponseEntity.ok(
                analyticsService.getSummary(shortCode));
    }

    @Operation(summary = "Get clicks grouped by country and city")
    @GetMapping("/{shortCode}/countries")
    public ResponseEntity<List<CountryStatsResponse>> getCountries(
            @PathVariable String shortCode) {

        return ResponseEntity.ok(
                analyticsService.getCountryStats(shortCode));
    }

    @Operation(summary = "Get clicks grouped by device type")
    @GetMapping("/{shortCode}/devices")
    public ResponseEntity<List<DeviceStatsResponse>> getDevices(
            @PathVariable String shortCode) {

        return ResponseEntity.ok(
                analyticsService.getDeviceStats(shortCode));
    }

    @Operation(summary = "Get clicks grouped by referrer source")
    @GetMapping("/{shortCode}/referrers")
    public ResponseEntity<List<ReferrerStatsResponse>> getReferrers(
            @PathVariable String shortCode) {

        return ResponseEntity.ok(
                analyticsService.getReferrerStats(shortCode));
    }

    @Operation(summary = "Get clicks per day for a date range")
    @GetMapping("/{shortCode}/timeline")
    public ResponseEntity<List<TimelineStatsResponse>> getTimeline(
            @PathVariable String shortCode,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
            LocalDateTime from,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
            LocalDateTime to) {

        return ResponseEntity.ok(
                analyticsService.getTimeline(shortCode, from, to));
    }
}