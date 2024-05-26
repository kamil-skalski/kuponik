package pl.kuponik.loyalty;

import java.util.UUID;

public interface LoyaltyFacade {

    void subtractPoints(UUID id, int pointsToSubtract);
}
