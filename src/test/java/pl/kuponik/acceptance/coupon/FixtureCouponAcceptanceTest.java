package pl.kuponik.acceptance.coupon;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.kuponik.application.dto.CreateLoyaltyAccountDto;
import pl.kuponik.application.LoyaltyAccountService;

import java.util.UUID;

@Component
class FixtureCouponAcceptanceTest {

    @Autowired
    private LoyaltyAccountService loyaltyAccountService;

    UUID createLoyaltyAccountWithPoints(int points) {
        var createLoyaltyAccountDto = new CreateLoyaltyAccountDto(UUID.randomUUID());
        var accountId = loyaltyAccountService.addAccount(createLoyaltyAccountDto);
        loyaltyAccountService.addPoints(accountId, points);
        return accountId;
    }

    UUID createLoyaltyAccountWithPoints() {
        return createLoyaltyAccountWithPoints(10000);
    }

    int getLoyaltyAccountPoints(UUID id) {
        return loyaltyAccountService.getAccount(id).points();
    }
}
