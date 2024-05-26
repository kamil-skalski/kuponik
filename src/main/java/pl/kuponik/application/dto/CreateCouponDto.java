package pl.kuponik.application.dto;

import jakarta.validation.constraints.NotNull;
import pl.kuponik.domain.NominalValue;

import java.util.UUID;

public record CreateCouponDto(
        @NotNull UUID loyaltyAccountId,
        @NotNull NominalValue nominalValue) {
}
