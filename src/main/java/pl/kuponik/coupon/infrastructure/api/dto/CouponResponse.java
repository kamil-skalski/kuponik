package pl.kuponik.coupon.infrastructure.api.dto;


import java.util.UUID;

public record CouponResponse(UUID id, UUID loyaltyAccountId, NominalValueApi nominalValue, boolean isActive) {
}
