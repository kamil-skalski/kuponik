package pl.kuponik.coupon.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import pl.kuponik.coupon.domain.exception.CouponNotActiveException;
import pl.kuponik.coupon.domain.exception.UnauthorizedCouponAccessException;

import java.util.UUID;

@Entity
@Getter
public class Coupon {

    @Id
    private UUID id;

    @NotNull
    private UUID loyaltyAccountId;

    @NotNull
    @Enumerated(EnumType.STRING)
    private NominalValue nominalValue;

    private boolean active;

    @Version
    private int version;

    public Coupon(UUID loyaltyAccountId, NominalValue nominalValue) {
        this.id = UUID.randomUUID();
        this.loyaltyAccountId = loyaltyAccountId;
        this.nominalValue = nominalValue;
        this.active = true;
    }

    protected Coupon() {
    }

    public void redeem(UUID loyaltyAccountId) {
        if (!isOwnedBy(loyaltyAccountId)) {
            throw new UnauthorizedCouponAccessException(this.id, loyaltyAccountId);
        }

        if (!isActive()) {
            throw new CouponNotActiveException(this.id);
        }

        this.active = false;
    }

    private boolean isOwnedBy(UUID loyaltyAccountId) {
        return this.loyaltyAccountId.equals(loyaltyAccountId);
    }
}
