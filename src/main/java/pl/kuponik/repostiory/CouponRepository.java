package pl.kuponik.repostiory;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.kuponik.model.Coupon;

import java.util.UUID;

public interface CouponRepository extends JpaRepository<Coupon, UUID> {
}
