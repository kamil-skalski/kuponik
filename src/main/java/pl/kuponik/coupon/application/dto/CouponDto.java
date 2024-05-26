package pl.kuponik.coupon.application.dto;


import pl.kuponik.coupon.domain.NominalValue;

import java.util.UUID;

public record CouponDto(UUID id, UUID loyaltyAccountId, NominalValue nominalValue, boolean isActive) {
}
