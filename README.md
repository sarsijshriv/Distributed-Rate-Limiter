<div align="center">

# 🚦 Distributed Token Bucket Rate Limiter

### Concurrency-safe distributed rate limiting using Spring Boot, Redis, and Lua

<p align="center">
  <img src="https://img.shields.io/badge/Java-17-orange?style=for-the-badge&logo=openjdk" />
  <img src="https://img.shields.io/badge/Spring_Boot-3.x-brightgreen?style=for-the-badge&logo=springboot" />
  <img src="https://img.shields.io/badge/Redis-Distributed_State-red?style=for-the-badge&logo=redis" />
  <img src="https://img.shields.io/badge/Lua-Atomicity-blue?style=for-the-badge&logo=lua" />
  <img src="https://img.shields.io/badge/k6-Load_Tested-purple?style=for-the-badge&logo=grafana" />
</p>

<p align="center">
  <b>Atomic token bucket implementation with distributed shared state and concurrency validation.</b>
</p>

</div>

---

# 🏗️ Architecture

```text
Client Request
        ↓
RateLimitController
        ↓
RequestContextFactory
        ↓
RateLimiterChain
        ↓
ApiKeyRateLimiter
        ↓
RedisTokenBucketStore
        ↓
Redis Lua Script (Atomic Execution)
        ↓
Redis Shared State
```

---

# ⚙️ Core Concepts Implemented

- Distributed shared state using Redis
- Atomic state transitions using Lua scripting
- Token bucket algorithm
- Stateless application nodes
- Multi-instance correctness
- Concurrency-safe token consumption
- Time-based refill logic
- API-key-based rate limiting

---

# 🧰 Tech Stack

| Technology | Purpose |
|---|---|
| Java 17 | Application runtime |
| Spring Boot | API framework |
| Redis | Distributed shared state |
| Lua | Atomic execution |
| Docker | Redis containerization |
| Maven | Build system |
| k6 | Load testing |

---

# ✨ Features

- Redis-backed distributed rate limiting
- Atomic token bucket implementation
- Configurable refill policy
- HTTP 429 handling
- Multi-instance support
- Load tested under concurrency
- Shared distributed correctness

---

# ⚡ Configuration

```yaml
rate-limiter:
  api-key:
    capacity: 1000
    refill-tokens: 1000
    refill-duration-millis: 1000
```

---

# 🐳 Running Redis

```bash
docker run -d --name redis_local -p 6379:6379 redis:7
```

---

# ▶️ Running Application

```bash
mvn spring-boot:run
```

---

# 🌐 Running Multiple Instances

```bash
mvn spring-boot:run "-Dspring-boot.run.arguments=--server.port=8081"
```

Both instances share the same Redis-backed rate limit state.

---

# 📡 API

## GET /check

### Header

```http
X-API-Key: key-123
```

### Responses

#### Allowed

```http
200 OK
```

#### Rejected

```http
429 TOO MANY REQUESTS
```

---

# 🔥 Load Testing

```javascript
import http from 'k6/http';
import { check } from 'k6';

export const options = {
    vus: 100,
    duration: '10s',
};

export default function () {

    const response = http.get(
        'http://127.0.0.1:8080/check',
        {
            headers: {
                'X-API-Key': 'key-123'
            }
        }
    );

    check(response, {
        'status is 200 or 429': (r) =>
            r.status === 200 || r.status === 429,
    });
}
```

Run:

```bash
k6 run load-test.js
```

---

# 📈 Benchmark Snapshot

Validated under:

- ~3600 requests/sec
- 100 concurrent virtual users
- Shared distributed Redis state
- Atomic Lua execution
- Multi-instance correctness

Observed behavior:

- Stable latency under load
- Correct 200 / 429 semantics
- No token leakage under concurrency
- Correct shared distributed limiting

---

# 🧠 Why Lua Matters

Without Lua:

```text
GET -> modify -> SET
```

can race under concurrency.

Lua ensures the entire state transition executes atomically inside Redis.

This prevents:

- Lost updates
- Double token consumption
- Concurrency leakage

---

# 🎯 What Was Learned

- Distributed coordination
- Stateless architecture
- Redis execution model
- Atomicity
- Concurrency correctness
- Load testing
- Performance validation
- Token bucket algorithms

---

# 🚀 Future Improvements

- Prometheus metrics
- Grafana dashboards
- Retry-After headers
- Sliding window algorithms
- Redis Cluster support
- Adaptive throttling

---

# 🏁 Key Takeaway

This project is fundamentally about:

```text
Concurrency-safe distributed state transitions.
```

The core engineering challenge solved here is:

```text
How to preserve correctness across distributed application instances under concurrent load.
```
