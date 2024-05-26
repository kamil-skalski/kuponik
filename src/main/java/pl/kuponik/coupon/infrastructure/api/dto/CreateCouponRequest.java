package pl.kuponik.coupon.infrastructure.api.dto;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record CreateCouponRequest(
        @NotNull UUID loyaltyAccountId,
        @NotNull NominalValueApi nominalValue) {
}
