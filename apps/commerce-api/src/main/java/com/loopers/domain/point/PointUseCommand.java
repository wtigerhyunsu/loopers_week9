package com.loopers.domain.point;

import java.math.BigInteger;

public record PointUseCommand(String userId, BigInteger payment) {
}
