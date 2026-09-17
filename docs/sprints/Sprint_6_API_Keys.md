# Sprint 6 — API Key System

**Date:** 16-09-2026
**Goal:** Third-party integration via API keys

## Files Created
| File                         | Purpose               |
|------------------------------|-----------------------|
| ApiKey.java                  | API keys table entity |
| ApiKeyRepository.java        | DB operations         |
| GenerateKeyRequest.java      | Input DTO             |
| ApiKeyResponse.java          | List keys response    |
| ApiKeyGeneratedResponse.java | Generate key response |
| ApiKeyService.java           | Business logic        |
| ApiKeyController.java        | API key CRUD APIs     |
| ApiKeyAuthFilter.java        | Request filter        |

## APIs Tested
| API                          | Result |
|------------------------------|--------|
| POST /api/v1/api-keys        | ✅ 201  |
| GET /api/v1/api-keys         | ✅ 200  |
| DELETE /api/v1/api-keys/{id} | ✅ 200  |
| URL shorten via API Key      | ✅ 201  |
| Revoked key access           | ✅ 401  |

## Status: ✅ COMPLETE