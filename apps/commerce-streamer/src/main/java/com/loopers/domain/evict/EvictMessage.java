package com.loopers.domain.evict;

public record EvictMessage(
    Long productId,
    String key
) { }
