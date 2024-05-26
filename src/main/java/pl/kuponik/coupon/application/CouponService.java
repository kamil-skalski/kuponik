package pl.kuponik.coupon.application;


import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.kuponik.coupon.application.dto.CouponDto;
import pl.kuponik.coupon.application.dto.CreateCouponDto;
import pl.kuponik.coupon.application.dto.RedeemCouponDto;
import pl.kuponik.coupon.application.exception.CouponNotFoundException;
import pl.kuponik.coupon.domain.Coupon;
import pl.kuponik.coupon.domain.repostiory.CouponRepository;
import pl.kuponik.loyalty.LoyaltyFacade;

import java.util.UUID;

@Service
@AllArgsConstructor
public class CouponService {

    private final CouponRepository couponRepository;
    private final LoyaltyFacade loyaltyFacade;

    @Transactional
    public UUID createCoupon(CreateCouponDto dto) {
        var coupon = new Coupon(dto.loyaltyAccountId(), dto.nominalValue());

        loyaltyFacade.subtractPoints(dto.loyaltyAccountId(), dto.nominalValue().getRequiredPoints());

        return couponRepository.save(coupon).getId();
    }

    @Transactional
    public void redeemCoupon(UUID couponId, RedeemCouponDto dto) {
        var coupon = couponRepository.findById(couponId)
                .orElseThrow(() -> new CouponNotFoundException(couponId));

        coupon.redeem(dto.loyaltyAccountId());

        couponRepository.save(coupon);
    }

    public CouponDto getCoupon(UUID id) {
        var coupon = couponRepository.findById(id)
                .orElseThrow(() -> new CouponNotFoundException(id));

        return new CouponDto(coupon.getId(), coupon.getLoyaltyAccountId(), coupon.getNominalValue(), coupon.isActive());
    }
}
