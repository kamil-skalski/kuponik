package pl.kuponik.domain.repostiory;

import pl.kuponik.domain.Coupon;

import java.util.Optional;
import java.util.UUID;

public interface CouponRepository {

    Optional<Coupon> findById(UUID id);

    Coupon save(Coupon coupon);
}
