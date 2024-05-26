package pl.kuponik.acceptance.loyaltyaccount;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.kuponik.application.dto.CreateLoyaltyAccountDto;
import pl.kuponik.application.LoyaltyAccountService;

import java.util.UUID;

@Component
class FixtureLoyaltyAccountAcceptanceTest {

    @Autowired
    private LoyaltyAccountService loyaltyAccountService;

    UUID createLoyaltyAccountWithPoints(int points) {
        var createLoyaltyAccountDto = new CreateLoyaltyAccountDto(UUID.randomUUID());
        var accountId = loyaltyAccountService.addAccount(createLoyaltyAccountDto);
        loyaltyAccountService.addPoints(accountId, points);
        return accountId;
    }

    UUID createLoyaltyAccount(UUID customerId, int points) {
        var createLoyaltyAccountDto = new CreateLoyaltyAccountDto(customerId);
        var accountId = loyaltyAccountService.addAccount(createLoyaltyAccountDto);
        loyaltyAccountService.addPoints(accountId, points);
        return accountId;
    }

    int getLoyaltyAccountPoints(UUID id) {
        return loyaltyAccountService.getAccount(id).points();
    }
}
