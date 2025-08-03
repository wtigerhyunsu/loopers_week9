package com.loopers.application.user;

public record UserCommand(
    String userId,
    String email,
    String birthday,
    String gender
) {
}
