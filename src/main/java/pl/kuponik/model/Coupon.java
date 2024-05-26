package pl.kuponik.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;


@Entity
@Getter
@Setter
public class Coupon {

    @Id
    private UUID id;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "loyalty_account_id")
    private LoyaltyAccount loyaltyAccount;

    @NotNull
    @Enumerated(EnumType.STRING)
    private NominalValue nominalValue;

    private boolean active;

    @Version
    private int version;
}
