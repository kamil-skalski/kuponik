package pl.kuponik.dto;

import java.util.UUID;

public record LoyaltyAccountDto(UUID id, UUID customerId, int points) {
}
