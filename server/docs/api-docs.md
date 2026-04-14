# Koinonia Daily — API Documentation

**Base URL:** `/api/v1`  
**Auth:** Bearer token (JWT) via `Authorization: Bearer <access_token>` header  
**Roles:** `USER`, `ADMIN`

---

## Response Envelopes

### SuccessResponse<T>
Standard successful response structure.
```json
{
  "success": true,
  "message": "string (optional)",
  "data": "T (optional)"
}
```

### PageResponse<T>
Standard paginated response structure.
```json
{
  "content": ["T"],
  "page": 0,
  "size": 50,
  "totalElements": 100,
  "totalPages": 2,
  "isFirst": true,
  "isLast": false
}
```

### ErrorResponse
Standard error response structure.
```json
{
  "success": false,
  "status": 400,
  "error": "Bad Request",
  "message": "Detailed error message",
  "path": "/api/v1/auth/login",
  "code": "ERROR_CODE",
  "errors": { "field": "validation error message" },
  "timestamp": "2024-03-20T12:00:00Z"
}
```

### Standard Error Codes
- `RESOURCE_NOT_FOUND` (404)
- `VALIDATION_ERROR` (422)
- `BAD_CREDENTIALS` (401)
- `RESOURCE_LOCKED` (409)
- `EMAIL_FAILED` (503)
- `INTERNAL_SERVER_ERROR` (500)
- `BAD_REQUEST` (400)
- `UNAUTHORIZED` (401)
- `USER_NOT_VERIFIED` (401)

---

## Auth

All `/auth/**` endpoints (except `change-password` and `logout`) are **public**.

### POST `/auth/register`
Register a new user. Sends OTP to email for verification.

**Request Body**
```json
{
  "firstName": "string",
  "lastName": "string",
  "email": "string",
  "password": "string"
}
```

### POST `/auth/login`
Authenticate a verified user and receive token pair.

**Request Body**
```json
{
  "email": "string",
  "password": "string"
}
```

**Response Data**
```json
{
  "accessToken": "string",
  "refreshToken": "string"
}
```

### POST `/auth/verify-email`
Verify user email with OTP.

**Request Body**
```json
{
  "email": "string",
  "otp": "string"
}
```

### POST `/auth/request-otp`
Request a new OTP for email verification.

**Request Body**
```json
{
  "email": "string"
}
```

### POST `/auth/forgot-password`
Request an OTP to reset password.

**Request Body**
```json
{
  "email": "string"
}
```

### POST `/auth/reset-password`
Reset password using OTP.

**Request Body**
```json
{
  "email": "string",
  "password": "string",
  "otp": "string"
}
```

### POST `/auth/change-password`
Change current password (Requires Auth).

**Request Body**
```json
{
  "currentPassword": "string",
  "newPassword": "string"
}
```

### POST `/auth/refresh-token`
Get a new access token using a refresh token.

**Request Body**
```json
{
  "refreshToken": "string"
}
```

**Response Data**
```json
{
  "accessToken": "string",
  "refreshToken": "string"
}
```

### POST `/auth/logout`
Invalidate a refresh token (Requires Auth).

**Request Body**
```json
{
  "refreshToken": "string"
}
```

---

## Account

### GET `/account/profile`
Get current user profile.

**Response Data**
```json
{
  "id": 1,
  "firstName": "string",
  "lastName": "string",
  "email": "string",
  "photoUrl": "string",
  "role": "USER | ADMIN",
  "createdAt": "timestamp",
  "updatedAt": "timestamp"
}
```

### PATCH `/account/profile`
Update current user profile.

**Request Body**
```json
{
  "firstName": "string",
  "lastName": "string",
  "photoUrl": "string"
}
```

### DELETE `/account`
Delete current user account.

---

## Teachings

### GET `/teachings`
List teachings (paginated).

**Query Params:** `page`, `size`

**Response Data:** `PageResponse<TeachingPageResponse>`

### GET `/teachings/search`
Search teachings (paginated).

**Query Params:** `q`, `page`, `size`

### GET `/teachings/{id}`
Get teaching details.

**Response Data**
```json
{
  "id": 1,
  "title": "string",
  "scripturalReferences": "string",
  "message": "string",
  "summary": "string",
  "audioUrl": "string",
  "videoUrl": "string",
  "thumbnailUrl": "string",
  "type": "SUNDAY_SERVICE | EXTERNAL_MINISTRATION | CONFERENCE | SPECIAL_SERVICE",
  "tags": "string",
  "series": { "id": 1, "name": "string" },
  "seriesPart": 1,
  "taughtAt": "timestamp",
  "createdAt": "timestamp",
  "updatedAt": "timestamp"
}
```

### POST `/teachings` (ADMIN)
Create a new teaching.

**Request Body**
```json
{
  "title": "string",
  "scripturalReferences": "string",
  "message": "string",
  "summary": "string",
  "audioUrl": "string",
  "videoUrl": "string",
  "thumbnailUrl": "string",
  "type": "SUNDAY_SERVICE | EXTERNAL_MINISTRATION | CONFERENCE | SPECIAL_SERVICE",
  "tags": "string",
  "seriesId": 1,
  "seriesPart": 1,
  "taughtAt": "timestamp"
}
```

### PUT `/teachings/{id}` (ADMIN)
Update an existing teaching.

### DELETE `/teachings/{id}` (ADMIN)
Delete a teaching.

---

## Series

### GET `/series`
List series (paginated).

**Response Data:** `PageResponse<SeriesPageResponse>`

### GET `/series/{id}`
Get series details including teachings.

**Response Data**
```json
{
  "id": 1,
  "name": "string",
  "description": "string",
  "thumbnailUrl": "string",
  "teachings": ["TeachingWithoutMessageProjection"]
}
```

### POST `/series` (ADMIN)
Create a series.

### PUT `/series/{id}` (ADMIN)
Update a series.

### DELETE `/series/{id}` (ADMIN)
Delete a series.

---

## Collections

### GET `/collections`
List collections (paginated).

### GET `/collections/{id}`
Get collection details.

### POST `/collections` (ADMIN)
Create a collection.

### PUT `/collections/{id}` (ADMIN)
Update a collection.

### POST `/collections/{id}/teachings` (ADMIN)
Add teaching to collection.

**Request Body**
```json
{ "teachingId": 1 }
```

### DELETE `/collections/{id}/teachings/{teachingId}` (ADMIN)
Remove teaching from collection.

### DELETE `/collections/{id}` (ADMIN)
Delete a collection.

---

## Bookmarks

### GET `/bookmarks`
Get bookmarks by category.

**Query Params:** `categoryId`, `page`, `size`

### POST `/bookmarks`
Create a bookmark.

**Request Body**
```json
{
  "teachingId": 1,
  "categoryId": 1,
  "note": "string"
}
```

### PATCH `/bookmarks/{id}/note`
Update bookmark note.

**Request Body**
```json
{ "note": "string" }
```

### DELETE `/bookmarks/{id}`
Delete a bookmark.

---

## Bookmark Categories

### GET `/bookmark-categories`
List categories for current user.

### GET `/bookmark-categories/{id}`
Get category by ID.

### POST `/bookmark-categories`
Create a category.

**Request Body**
```json
{ "name": "string" }
```

### PATCH `/bookmark-categories/{id}/name`
Update category name.

### DELETE `/bookmark-categories/{id}`
Delete a category.

---

## History

### GET `/histories`
Get current user teaching history.

### PATCH `/histories/{id}/marked-read`
Mark history item as read/unread.

**Request Body**
```json
{ "isMarkedRead": true }
```

### DELETE `/histories/{id}`
Delete a history item.

---

## Transcripts (ADMIN Only)

### GET `/transcripts`
List transcripts.

### GET `/transcripts/search`
Search transcripts.

**Query Params:** `q`

### GET `/transcripts/{id}`
Get transcript details.

### POST `/transcripts`
Create a transcript.

**Request Body**
```json
{ "title": "string", "message": "string" }
```

### PUT `/transcripts/{id}`
Update a transcript.

### DELETE `/transcripts/{id}`
Delete a transcript.

---

## File (ADMIN Only)

### POST `/file/presign`
Generate a presigned S3 upload URL.

**Request Body**
```json
{ "fileExtension": "jpg | jpeg | png | gif | webp" }
```

**Response Data**
```json
{
  "presignedUrl": "string",
  "publicUrl": "string",
  "key": "string",
  "requiredHeaders": { "header-name": ["header-value"] }
}
```

### DELETE `/file`
Delete a file from storage.

**Query Params:** `objectKey`
