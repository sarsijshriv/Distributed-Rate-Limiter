# Distributed Rate Limiter

A production-style distributed rate limiter built using:

* Java 17
* Spring Boot
* Redis
* Docker

The goal of this project is to understand and implement:

* Distributed shared state management
* Concurrency-safe rate limiting
* Redis-based coordination
* Token bucket algorithm
* Lua-based atomic operations
* Clean backend architecture

---

# Current Architecture

Current request flow:

```text
HTTP Request
    ↓
RateLimitController
    ↓
RequestContextFactory
    ↓
RequestContext
    ↓
RateLimiterChain
    ↓
ApiKeyRateLimiter
    ↓
RateLimitStore
```

Redis-backed token consumption logic is the next implementation step.

---

# Project Structure

```text
com.sarsij.ratelimiter

├── context/
│   ├── RequestContext
│   └── RequestContextFactory
│
├── controller/
│   └── RateLimitController
│
├── key/
│   └── RateLimitKeyBuilder
│
├── limiter/
│   ├── RateLimiter
│   ├── RateLimiterChain
│   └── ApiKeyRateLimiter
│
└── store/
    └── RateLimitStore
```

---

# Core Design Decisions

## 1. RequestContext Abstraction

The rate limiter logic does not directly depend on `HttpServletRequest`.

Instead:

```text
HTTP Request
→ RequestContextFactory
→ RequestContext
```

This keeps limiter logic:

* Framework-independent
* Easier to test
* Easier to extend

---

## 2. Centralized Redis Key Generation

`RateLimitKeyBuilder` is responsible for generating standardized Redis keys.

Example:

```text
rate_limit:apikey:key-123
rate_limit:ip:127.0.0.1
rate_limit:endpoint:/payments
```

This prevents inconsistent key generation across the system.

---

## 3. Extensible Limiter Architecture

The system uses a `RateLimiter` interface:

```java
public interface RateLimiter {
    boolean allow(RequestContext context);
}
```

This allows multiple limiter implementations:

* API key limiter
* IP limiter
* Endpoint limiter
* Global limiter

without changing the core architecture.

---

## 4. RateLimiterChain

`RateLimiterChain` orchestrates multiple limiter checks.

Flow:

```text
ANY limiter rejects → request rejected
ALL limiters pass → request allowed
```

---

# Planned Features

## Distributed State Management

* Redis-backed token storage
* Shared state across multiple application instances
* Atomic token updates

---

## Token Bucket Algorithm

Planned implementation:

* Capacity-based limiting
* Time-based refill logic
* Configurable refill rate

---

## Lua-Based Atomicity

Redis Lua scripts will be used to ensure:

* Atomic token consumption
* Race-condition safety
* Correct behavior under concurrency

---

## Future Improvements

* Spring filter/interceptor integration
* Config-driven limits
* Multiple limiter dimensions
* Metrics and observability
* Load testing
* Multi-instance deployment

---

# Running the Project

## Start Redis

```bash
docker run -d --name redis_local -p 6379:6379 redis:7
```

---

## Start Application

```bash
mvn spring-boot:run
```

---

## Test Endpoint

PowerShell:

```powershell
(Invoke-WebRequest -Uri http://localhost:8080/check -Headers @{ "X-API-Key" = "key-123" }).Content
```

Expected:

```text
ALLOWED
```

---

# Current Status

## Completed

* Spring Boot setup
* Redis setup using Docker
* Request normalization pipeline
* RequestContext abstraction
* Centralized Redis key generation
* Extensible limiter architecture
* Rate limiter orchestration layer
* Distributed store abstraction

## In Progress

* Redis-backed token consumption
* Token bucket implementation
* Lua atomic operations
* Multi-instance distributed testing
