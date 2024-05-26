package pl.kuponik.service;

import pl.kuponik.domain.Coupon;
import pl.kuponik.domain.repostiory.CouponRepository;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

class InMemoryCouponRepository implements CouponRepository {

    private final Map<UUID, Coupon> store = new HashMap<>();

    @Override
    public Optional<Coupon> findById(UUID id) {
        return Optional.ofNullable(store.get(id));
    }

    @Override
    public Coupon save(final Coupon coupon) {
        store.put(coupon.getId(), coupon);
        return coupon;
    }

    void deleteAll() {
        store.clear();
    }
}