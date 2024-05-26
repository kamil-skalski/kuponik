package pl.kuponik.coupon.application;

import pl.kuponik.loyalty.LoyaltyFacade;

import java.util.UUID;

class LoyaltyFacadeStub implements LoyaltyFacade {

    @Override
    public void subtractPoints(UUID id, int pointsToSubtract) {

    }
}
