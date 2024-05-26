package pl.kuponik.infrastructure.api.dto;

import java.util.UUID;

public record LoyaltyAccountResponse(UUID id, UUID customerId, int points) {
}
