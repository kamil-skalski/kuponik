package pl.kuponik.coupon.application;


import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import pl.kuponik.coupon.application.dto.CreateCouponDto;
import pl.kuponik.coupon.application.dto.RedeemCouponDto;
import pl.kuponik.coupon.application.exception.CouponNotFoundException;
import pl.kuponik.coupon.domain.NominalValue;
import pl.kuponik.coupon.domain.exception.CouponNotActiveException;
import pl.kuponik.coupon.domain.exception.UnauthorizedCouponAccessException;
import pl.kuponik.loyalty.LoyaltyFacade;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

class CouponServiceTest {

    LoyaltyFacade loyaltyFacade = new LoyaltyFacadeStub();
    InMemoryCouponRepository couponRepository = new InMemoryCouponRepository();
    CouponService couponService = new CouponService(couponRepository, loyaltyFacade);
    UUID loyaltyAccountId = UUID.randomUUID();

    @AfterEach
    void cleanUp() {
        couponRepository.deleteAll();
    }

    @Test
    void shouldCreateCouponSuccessfully() {
        // given
        var createCouponDto = new CreateCouponDto(loyaltyAccountId, NominalValue.TWENTY);

        // when
        var couponId = couponService.createCoupon(createCouponDto);

        // then
        var retrievedCoupon = couponService.getCoupon(couponId);
        assertThat(retrievedCoupon).isNotNull();
        assertThat(retrievedCoupon.isActive()).isTrue();
        assertThat(retrievedCoupon.nominalValue()).isEqualTo(NominalValue.TWENTY);
        assertThat(retrievedCoupon.loyaltyAccountId()).isEqualTo(loyaltyAccountId);
    }

    @Test
    void shouldGetCouponByIdSuccessfully() {
        // given
        var createCouponDto = new CreateCouponDto(loyaltyAccountId, NominalValue.TWENTY);
        var couponId = couponService.createCoupon(createCouponDto);

        // when
        var retrievedCoupon = couponService.getCoupon(couponId);

        // then
        assertThat(retrievedCoupon).isNotNull();
        assertThat(retrievedCoupon.id()).isEqualTo(couponId);
        assertThat(retrievedCoupon.isActive()).isTrue();
        assertThat(retrievedCoupon.nominalValue()).isEqualTo(NominalValue.TWENTY);
        assertThat(retrievedCoupon.loyaltyAccountId()).isEqualTo(loyaltyAccountId);
    }

    @Test
    void shouldThrowExceptionWhenGettingNonExistingCoupon() {
        // given
        var nonExistentCouponId = UUID.randomUUID();

        // expected
        assertThatThrownBy(() -> couponService.getCoupon(nonExistentCouponId))
                .isInstanceOf(CouponNotFoundException.class);
    }

    @Test
    void shouldRedeemCouponSuccessfully() {
        // given
        var couponId = createCoupon(loyaltyAccountId, NominalValue.TWENTY);
        var redeemRequest = new RedeemCouponDto(loyaltyAccountId);

        // when
        couponService.redeemCoupon(couponId, redeemRequest);

        // then
        var redeemedCoupon = couponService.getCoupon(couponId);
        assertThat(redeemedCoupon).isNotNull();
        assertThat(redeemedCoupon.id()).isEqualTo(couponId);
        assertThat(redeemedCoupon.isActive()).isFalse();
        assertThat(redeemedCoupon.nominalValue()).isEqualTo(NominalValue.TWENTY);
        assertThat(redeemedCoupon.loyaltyAccountId()).isEqualTo(loyaltyAccountId);
    }

    @Test
    void shouldThrowExceptionWhenRedeemingNonExistentCoupon() {
        // given
        var nonExistentCouponId = UUID.randomUUID();
        var redeemRequest = new RedeemCouponDto(loyaltyAccountId);

        // expected
        assertThatThrownBy(() -> couponService.redeemCoupon(nonExistentCouponId, redeemRequest))
                .isInstanceOf(CouponNotFoundException.class);
    }

    @Test
    void shouldThrowExceptionWhenUnauthorizedRedemptionAttempted() {
        // given
        var anotherAccountId = UUID.randomUUID();
        var couponId = createCoupon(loyaltyAccountId, NominalValue.TWENTY);
        var redeemRequest = new RedeemCouponDto(anotherAccountId);

        // expected
        assertThatThrownBy(() -> couponService.redeemCoupon(couponId, redeemRequest))
                .isInstanceOf(UnauthorizedCouponAccessException.class);
    }

    @Test
    void shouldThrowExceptionWhenRedeemingInactiveCoupon() {
        // given
        var couponId = createCoupon(loyaltyAccountId, NominalValue.TWENTY);
        var redeemRequest = new RedeemCouponDto(loyaltyAccountId);
        couponService.redeemCoupon(couponId, redeemRequest);

        // expected
        assertThatThrownBy(() -> couponService.redeemCoupon(couponId, redeemRequest))
                .isInstanceOf(CouponNotActiveException.class);
    }

    private UUID createCoupon(UUID accountId, NominalValue nominalValue) {
        var createCouponDto = new CreateCouponDto(accountId, nominalValue);
        return couponService.createCoupon(createCouponDto);
    }

}