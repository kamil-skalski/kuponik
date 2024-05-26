package pl.kuponik.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Version;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import pl.kuponik.exception.InsufficientPointsException;

import java.util.UUID;

@Entity
@Getter
public class LoyaltyAccount {

    @Id
    private UUID id;

    @NotNull
    private UUID customerId;

    @NotNull
    private Integer points;

    @Version
    private int version;

    public LoyaltyAccount(UUID customerId) {
        this.id = UUID.randomUUID();
        this.customerId = customerId;
        this.points = 0;
    }

    protected LoyaltyAccount() {
    }

    public void addPoints(int pointsToAdd) {
        points += pointsToAdd;
    }

    public void subtractPoints(int pointsToSubtract) {
        if (points < pointsToSubtract) {
            throw new InsufficientPointsException(id);
        }
        points -= pointsToSubtract;
    }
}
