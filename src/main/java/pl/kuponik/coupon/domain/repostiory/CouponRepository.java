package pl.kuponik.coupon.domain.repostiory;

import pl.kuponik.coupon.domain.Coupon;

import java.util.Optional;
import java.util.UUID;

public interface CouponRepository {

    Optional<Coupon> findById(UUID id);

    Coupon save(Coupon coupon);
}
