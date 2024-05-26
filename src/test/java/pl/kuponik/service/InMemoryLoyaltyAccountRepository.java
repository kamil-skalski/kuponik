package pl.kuponik.service;

import pl.kuponik.model.LoyaltyAccount;
import pl.kuponik.repostiory.LoyaltyAccountRepository;

import java.util.*;

class InMemoryLoyaltyAccountRepository implements LoyaltyAccountRepository {

    private final Map<UUID, LoyaltyAccount> store = new HashMap<>();

    @Override
    public Optional<LoyaltyAccount> findById(UUID id) {
        return Optional.ofNullable(store.get(id));
    }

    @Override
    public LoyaltyAccount save(final LoyaltyAccount loyaltyAccount) {
        store.put(loyaltyAccount.getId(), loyaltyAccount);
        return loyaltyAccount;
    }

    @Override
    public List<LoyaltyAccount> findByCustomerId(UUID customerId) {
        return store.values().stream()
                .filter(account -> account.getCustomerId().equals(customerId))
                .toList();
    }

    void deleteAll() {
        store.clear();
    }
}
