package pl.kuponik.repostiory;

import pl.kuponik.model.Coupon;

import java.util.Optional;
import java.util.UUID;

public interface CouponRepository {

    Optional<Coupon> findById(UUID id);

    Coupon save(Coupon coupon);
}
