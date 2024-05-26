package pl.kuponik.infrastructure.api.dto;

import jakarta.validation.constraints.NotNull;
import pl.kuponik.domain.NominalValue;

import java.util.UUID;

public record CreateCouponRequest(
        @NotNull UUID loyaltyAccountId,
        @NotNull NominalValueApi nominalValue) {
}
