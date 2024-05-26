package pl.kuponik.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Version;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import pl.kuponik.domain.exception.InsufficientPointsException;

import java.util.UUID;

@Entity
@Getter
public class LoyaltyAccount {

    @Id
    private UUID id;

    @NotNull
    private UUID customerId;

    @NotNull
    private LoyaltyPoints points;

    @Version
    private int version;

    public LoyaltyAccount(UUID customerId) {
        this.id = UUID.randomUUID();
        this.customerId = customerId;
        this.points = LoyaltyPoints.ZERO;
    }

    protected LoyaltyAccount() {
    }

    public void addPoints(LoyaltyPoints pointsToAdd) {
        points = points.add(pointsToAdd);
    }

    public void subtractPoints(LoyaltyPoints pointsToSubtract) {
        if (!points.isGreaterOrEqualThan(pointsToSubtract)) {
            throw new InsufficientPointsException(id);
        }
        points = points.subtract(pointsToSubtract);
    }
}
