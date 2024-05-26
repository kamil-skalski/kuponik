package pl.kuponik.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.kuponik.dto.CouponDto;
import pl.kuponik.dto.CreateCouponDto;
import pl.kuponik.dto.RedeemCouponRequest;
import pl.kuponik.exception.CouponNotFoundException;
import pl.kuponik.exception.LoyaltyAccountNotFoundException;
import pl.kuponik.model.Coupon;
import pl.kuponik.model.LoyaltyAccount;
import pl.kuponik.repostiory.CouponRepository;
import pl.kuponik.repostiory.LoyaltyAccountRepository;

import java.util.UUID;

@Service
public class CouponService {

    @Autowired
    private CouponRepository couponRepository;

    @Autowired
    private LoyaltyAccountService loyaltyAccountService;

    @Autowired
    private LoyaltyAccountRepository loyaltyAccountRepository;

    @Transactional
    public UUID createCoupon(CreateCouponDto request) {
        var coupon = new Coupon(findLoyaltyAccountById(request.loyaltyAccountId()), request.nominalValue());

        loyaltyAccountService.subtractPoints(request.loyaltyAccountId(), request.nominalValue().getRequiredPoints());

        return couponRepository.save(coupon).getId();
    }

    @Transactional
    public void redeemCoupon(UUID couponId, RedeemCouponRequest request) {
        var coupon = couponRepository.findById(couponId)
                .orElseThrow(() -> new CouponNotFoundException(couponId));

        coupon.redeem(request.loyaltyAccountId());

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
