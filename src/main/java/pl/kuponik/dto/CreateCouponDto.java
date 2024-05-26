package pl.kuponik.dto;

import jakarta.validation.constraints.NotNull;
import pl.kuponik.model.NominalValue;

import java.util.UUID;

public record CreateCouponDto(
        @NotNull UUID loyaltyAccountId,
        @NotNull NominalValue nominalValue) {
}
