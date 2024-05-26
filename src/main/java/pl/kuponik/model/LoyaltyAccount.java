package pl.kuponik.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Version;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Entity
@Getter
@Setter
public class LoyaltyAccount {

    @Id
    private UUID id;

    @NotNull
    private UUID customerId;

    @NotNull
    private Integer points;

    @Version
    private int version;
}
