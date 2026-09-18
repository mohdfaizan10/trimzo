# Sprint 7 — Error Handling Polish

**Date:** 18-09-2026
**Goal:** Proper HTTP status codes and custom exceptions

## Files Created
| File                       | Purpose              |
|----------------------------|----------------------|
| UrlNotFoundException.java  | 404 — URL not found  |
| UrlExpiredException.java   | 410 — URL expired    |
| UrlInactiveException.java  | 403 — URL inactive   |
| AccessDeniedException.java | 403 — Access denied  |
| UserNotFoundException.java | 404 — User not found |

## Error Codes Fixed
| Situation     | Before | After |
|---------------|--------|-------|
| URL not found | 409    | 404 ✅ |
| URL expired   | 409    | 410 ✅ |
| URL inactive  | 409    | 403 ✅ |
| Access denied | 409    | 403 ✅ |

## Status: ✅ COMPLETE