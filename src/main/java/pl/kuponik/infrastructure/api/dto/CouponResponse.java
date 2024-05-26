package pl.kuponik.infrastructure.api.dto;


import java.util.UUID;

public record CouponResponse(UUID id, UUID loyaltyAccountId, NominalValueApi nominalValue, boolean isActive) {
}
