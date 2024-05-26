package pl.kuponik.coupon.infrastructure.api.dto;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record RedeemCouponRequest(@NotNull UUID loyaltyAccountId) {
}
