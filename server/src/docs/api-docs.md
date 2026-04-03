# Koinonia Daily — API Documentation

**Base URL:** `/api/v1`  
**Auth:** Bearer token (JWT) via `Authorization: Bearer <access_token>` header  
**Roles:** `USER`, `ADMIN`

---

## Auth

All `/auth/**` endpoints (except `profile` and `change-password`) are **public**.

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

**Response `200`**
```json
{ "success": true, "message": "Registration successful. OTP has been sent to your email for verification." }
```

---

### POST `/auth/login`
Authenticate a verified user and receive token pair.

**Request Body**
```json
{
  "email": "string",
  "password": "string"
}
```

**Response `200`**
```json
{
  "success": true,
  "message": "Login Successful",
  "data": {
    "accessToken": "string",
    "refreshToken": "string"
  }
}
```

**Error Codes**
- `BAD_CREDENTIALS` — wrong email/password
- `USER_NOT_VERIFIED` — email not yet verified

---

### POST `/auth/verify-email`
Verify user email with OTP.

**Request Body**
```json
{
  "email": "string",
  "otp": "string"
}
```

**Response `200`**
```json
{ "success": true, "message": "OTP verified successfully" }
```

**Error Codes**
- `UNAUTHORIZED` — OTP is invalid, revoked, or expired
- `RESOURCE_NOT_FOUND` — no account found for the provided email
