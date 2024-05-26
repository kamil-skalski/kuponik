package pl.kuponik.loyalty;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.kuponik.loyalty.dto.CreateLoyaltyAccountDto;

import java.util.UUID;

@Component
public class FixtureLoyaltyAccountAcceptanceTest {

    @Autowired
    private LoyaltyAccountService loyaltyAccountService;

    public UUID createLoyaltyAccountWithPoints(int points) {
        var createLoyaltyAccountDto = new CreateLoyaltyAccountDto(UUID.randomUUID());
        var accountId = loyaltyAccountService.addAccount(createLoyaltyAccountDto);
        loyaltyAccountService.addPoints(accountId, points);
        return accountId;
    }

    public UUID createLoyaltyAccount(UUID customerId, int points) {
        var createLoyaltyAccountDto = new CreateLoyaltyAccountDto(customerId);
        var accountId = loyaltyAccountService.addAccount(createLoyaltyAccountDto);
        loyaltyAccountService.addPoints(accountId, points);
        return accountId;
    }

    public int getLoyaltyAccountPoints(UUID id) {
        return loyaltyAccountService.getAccount(id).points();
    }
}
