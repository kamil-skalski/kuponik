package pl.kuponik.application;


import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.kuponik.application.dto.CouponDto;
import pl.kuponik.application.dto.CreateCouponDto;
import pl.kuponik.application.dto.RedeemCouponDto;
import pl.kuponik.application.exception.CouponNotFoundException;
import pl.kuponik.domain.Coupon;
import pl.kuponik.domain.LoyaltyAccount;
import pl.kuponik.application.exception.LoyaltyAccountNotFoundException;
import pl.kuponik.domain.repostiory.CouponRepository;
import pl.kuponik.domain.repostiory.LoyaltyAccountRepository;

import java.util.UUID;

@Service
@AllArgsConstructor
public class CouponService {

    private final CouponRepository couponRepository;
    private final LoyaltyAccountService loyaltyAccountService;
    private final LoyaltyAccountRepository loyaltyAccountRepository;

    @Transactional
    public UUID createCoupon(CreateCouponDto dto) {
        var coupon = new Coupon(findLoyaltyAccountById(dto.loyaltyAccountId()), dto.nominalValue());

        loyaltyAccountService.subtractPoints(dto.loyaltyAccountId(), dto.nominalValue().getRequiredPoints());

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

        return new CouponDto(coupon.getId(), coupon.getLoyaltyAccount().getId(), coupon.getNominalValue(), coupon.isActive());
    }

    private LoyaltyAccount findLoyaltyAccountById(UUID loyaltyAccountId) throws LoyaltyAccountNotFoundException {
        return loyaltyAccountRepository.findById(loyaltyAccountId)
                .orElseThrow(() -> new LoyaltyAccountNotFoundException(loyaltyAccountId));
    }
}
