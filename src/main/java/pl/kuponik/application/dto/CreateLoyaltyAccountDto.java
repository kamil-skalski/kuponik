package pl.kuponik.application.dto;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record CreateLoyaltyAccountDto(
        @NotNull UUID customerId) {
}
