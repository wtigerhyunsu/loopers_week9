package com.loopers.data_platform;

public record PlatformSendEvent(String type, Long id, String payload) {
}
