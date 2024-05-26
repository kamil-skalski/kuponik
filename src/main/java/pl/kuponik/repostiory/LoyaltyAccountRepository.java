package pl.kuponik.repostiory;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.kuponik.model.LoyaltyAccount;

import java.util.List;
import java.util.UUID;

public interface LoyaltyAccountRepository extends JpaRepository<LoyaltyAccount, UUID> {

    List<LoyaltyAccount> findByCustomerId(UUID customerId);
}
