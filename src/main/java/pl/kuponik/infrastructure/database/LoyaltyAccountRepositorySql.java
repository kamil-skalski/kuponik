package pl.kuponik.infrastructure.database;

import lombok.AllArgsConstructor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import pl.kuponik.domain.LoyaltyAccount;
import pl.kuponik.domain.repostiory.LoyaltyAccountRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@AllArgsConstructor
public class LoyaltyAccountRepositorySql implements LoyaltyAccountRepository {

    private final LoyaltyAccountRepositoryJpa loyaltyAccountRepositoryJpa;

    @Override
    public Optional<LoyaltyAccount> findById(UUID id) {
        return loyaltyAccountRepositoryJpa.findById(id);
    }

    @Override
    public LoyaltyAccount save(LoyaltyAccount loyaltyAccount) {
        return loyaltyAccountRepositoryJpa.save(loyaltyAccount);
    }

    @Override
    public List<LoyaltyAccount> findByCustomerId(UUID customerId) {
        return loyaltyAccountRepositoryJpa.findByCustomerId(customerId);
    }
}

@Repository
interface LoyaltyAccountRepositoryJpa extends CrudRepository<LoyaltyAccount, UUID> {

    List<LoyaltyAccount> findByCustomerId(UUID customerId);
}