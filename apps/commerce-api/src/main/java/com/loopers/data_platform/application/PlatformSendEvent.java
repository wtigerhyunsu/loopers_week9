package com.loopers.data_platform.application;

public record PlatformSendEvent(String type, Long id, String payload) {
}
