package pl.kuponik.coupon.infrastructure.database;

import lombok.AllArgsConstructor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import pl.kuponik.coupon.domain.Coupon;
import pl.kuponik.coupon.domain.repostiory.CouponRepository;

import java.util.Optional;
import java.util.UUID;

@Repository
@AllArgsConstructor
class SqlCouponRepository implements CouponRepository {

    private final CouponRepositoryJpa couponRepositoryJpa;

    @Override
    public Optional<Coupon> findById(UUID id) {
        return couponRepositoryJpa.findById(id);
    }

    @Override
    public Coupon save(Coupon coupon) {
        return couponRepositoryJpa.save(coupon);
    }
}

@Repository
interface CouponRepositoryJpa extends CrudRepository<Coupon, UUID> {
}