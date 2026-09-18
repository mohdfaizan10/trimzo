# Sprint 3 — URL Shortening

**Date:** 04-07-2026
**Goal:** URL shortening + redirect working

## Files Created
| File                              | Purpose                         |
|-----------------------------------|---------------------------------|
| Base62Encoder.java                | DSA — Generating short code     |
| Url.java                          | URL database entity             |
| UrlRepository.java                | DB operations                   |
| CreateUrlRequest.java             | Input format                    |
| UrlResponse.java                  | Output format                   |
| UrlService.java                   | Business logic                  |
| UrlController.java                | URL APIs                        |
| RedirectController.java           | Short URL redirect              |
| V5__alter_short_code_nullable.sql | DB fix                          |

## APIs Tested
| API                        | Status    | Result |
|----------------------------|-----------|--------|
| POST /api/v1/urls          | 201       | ✅      | 
| GET /api/v1/urls           | 200       | ✅      | 
| Duplicate URL              | Same code | ✅      |
| DELETE /api/v1/urls/{code} | 200       | ✅      |
| Redirect /{shortCode}      | 302       | ✅      |

## DSA Used
- Base62 Encoding
- 62^6 = 56 billion unique URLs possible

## Status: ✅ COMPLETE