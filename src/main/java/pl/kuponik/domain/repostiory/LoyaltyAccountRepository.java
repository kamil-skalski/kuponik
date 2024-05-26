package pl.kuponik.domain.repostiory;

import pl.kuponik.domain.LoyaltyAccount;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface LoyaltyAccountRepository {

    Optional<LoyaltyAccount> findById(UUID id);

    LoyaltyAccount save(LoyaltyAccount loyaltyAccount);

    List<LoyaltyAccount> findByCustomerId(UUID customerId);
}
