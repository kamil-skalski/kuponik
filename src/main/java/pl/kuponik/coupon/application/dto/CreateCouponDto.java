package pl.kuponik.coupon.application.dto;

import jakarta.validation.constraints.NotNull;
import pl.kuponik.coupon.domain.NominalValue;

import java.util.UUID;

public record CreateCouponDto(
        @NotNull UUID loyaltyAccountId,
        @NotNull NominalValue nominalValue) {
}
