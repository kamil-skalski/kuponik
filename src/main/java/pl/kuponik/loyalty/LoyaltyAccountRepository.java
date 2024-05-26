package pl.kuponik.loyalty;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

interface LoyaltyAccountRepository {

    Optional<LoyaltyAccount> findById(UUID id);

    LoyaltyAccount save(LoyaltyAccount loyaltyAccount);

    List<LoyaltyAccount> findByCustomerId(UUID customerId);
}
