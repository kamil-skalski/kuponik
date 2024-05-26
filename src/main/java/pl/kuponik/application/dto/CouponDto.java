package pl.kuponik.application.dto;


import pl.kuponik.domain.NominalValue;

import java.util.UUID;

public record CouponDto(UUID id, UUID loyaltyAccountId, NominalValue nominalValue, boolean isActive) {
}
