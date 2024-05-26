package pl.kuponik.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.kuponik.dto.CouponDto;
import pl.kuponik.dto.CreateCouponDto;
import pl.kuponik.dto.RedeemCouponRequest;
import pl.kuponik.exception.*;
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
        var loyaltyAccountDto = loyaltyAccountService.getAccount(request.loyaltyAccountId());

        if (loyaltyAccountDto.points() < request.nominalValue().getRequiredPoints()) {
            throw new InsufficientPointsException(request.loyaltyAccountId());
        }

        var coupon = new Coupon();
        coupon.setId(UUID.randomUUID());
        coupon.setLoyaltyAccount(findLoyaltyAccountById(request.loyaltyAccountId()));
        coupon.setActive(true);
        coupon.setNominalValue(request.nominalValue());

        loyaltyAccountService.subtractPoints(request.loyaltyAccountId(), request.nominalValue().getRequiredPoints());

        return couponRepository.save(coupon).getId();
    }

    @Transactional
    public void redeemCoupon(UUID couponId, RedeemCouponRequest request) {
        var coupon = couponRepository.findById(couponId)
                .orElseThrow(() -> new CouponNotFoundException(couponId));

        if (!coupon.getLoyaltyAccount().getId().equals(request.loyaltyAccountId())) {
            throw new UnauthorizedCouponAccessException(couponId, request.loyaltyAccountId());
        }

        if (!coupon.isActive()) {
            throw new CouponNotActiveException(couponId);
        }

        coupon.setActive(false);
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
