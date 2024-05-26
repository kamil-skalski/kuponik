package pl.kuponik.repostiory;

import lombok.AllArgsConstructor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import pl.kuponik.model.Coupon;

import java.util.Optional;
import java.util.UUID;

@Repository
@AllArgsConstructor
public class SqlCouponRepository implements CouponRepository {

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