local bucketKey = KEYS[1]

local bucketCapacity = tonumber(ARGV[1])
local refillTokenCount = tonumber(ARGV[2])
local refillDurationMillis = tonumber(ARGV[3])
local currentTimestampMillis = tonumber(ARGV[4])

local bucketState = redis.call(
    "HMGET",
    bucketKey,
    "available_tokens",
    "last_refill_timestamp"
)

local availableTokens =
    tonumber(bucketState[1])

local lastRefillTimestamp =
    tonumber(bucketState[2])

if not availableTokens then
    availableTokens = bucketCapacity
    lastRefillTimestamp =
        currentTimestampMillis
end

local elapsedTimeMillis =
    currentTimestampMillis
    - lastRefillTimestamp

local refillIntervalsPassed =
    math.floor(
        elapsedTimeMillis
        / refillDurationMillis
    )

local tokensToRefill =
    refillIntervalsPassed
    * refillTokenCount

if tokensToRefill > 0 then

    availableTokens = math.min(
        bucketCapacity,
        availableTokens + tokensToRefill
    )

    lastRefillTimestamp =
        currentTimestampMillis
end

if availableTokens <= 0 then

    redis.call(
        "HMSET",
        bucketKey,
        "available_tokens",
        availableTokens,
        "last_refill_timestamp",
        lastRefillTimestamp
    )

    return 0
end

availableTokens =
    availableTokens - 1

redis.call(
    "HMSET",
    bucketKey,
    "available_tokens",
    availableTokens,
    "last_refill_timestamp",
    lastRefillTimestamp
)

return 1