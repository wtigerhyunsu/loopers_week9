package com.loopers.infrastructure.log;

public record LogMessage(
    String userId,
    String message
) {
}
