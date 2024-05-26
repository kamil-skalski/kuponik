package pl.kuponik.service;


import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import pl.kuponik.domain.exception.CouponNotActiveException;
import pl.kuponik.application.exception.CouponNotFoundException;
import pl.kuponik.application.CouponService;
import pl.kuponik.application.LoyaltyAccountService;
import pl.kuponik.domain.exception.InsufficientPointsException;
import pl.kuponik.application.exception.LoyaltyAccountNotFoundException;
import pl.kuponik.domain.exception.UnauthorizedCouponAccessException;
import pl.kuponik.application.dto.CreateCouponDto;
import pl.kuponik.application.dto.CreateLoyaltyAccountDto;
import pl.kuponik.application.dto.RedeemCouponDto;
import pl.kuponik.domain.NominalValue;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

class CouponServiceTest {

    InMemoryLoyaltyAccountRepository loyaltyAccountRepository = new InMemoryLoyaltyAccountRepository();
    LoyaltyAccountService loyaltyAccountService = new LoyaltyAccountService(loyaltyAccountRepository);
    InMemoryCouponRepository couponRepository = new InMemoryCouponRepository();
    CouponService couponService = new CouponService(couponRepository, loyaltyAccountService, loyaltyAccountRepository);

    @AfterEach
    void cleanUp() {
        couponRepository.deleteAll();
    }

    @Test
    void shouldCreateCouponSuccessfully() {
        // given
        var loyaltyAccountId = createLoyaltyAccountWithPoints(1000);
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
    void shouldThrowExceptionWhenCreatingCouponAndLoyaltyAccountNotFound() {
        // given
        var nonExistentLoyaltyAccountId = UUID.randomUUID();
        var createCouponDto = new CreateCouponDto(nonExistentLoyaltyAccountId, NominalValue.TWENTY);

        // expected
        assertThatThrownBy(() -> couponService.createCoupon(createCouponDto))
                .isInstanceOf(LoyaltyAccountNotFoundException.class);
    }

    @Test
    void shouldThrowExceptionWhenCreatingCouponWithInsufficientPoints() {
        // given
        var loyaltyAccountId = createLoyaltyAccountWithPoints(10);
        var createCouponDto = new CreateCouponDto(loyaltyAccountId, NominalValue.FIFTY);

        // expected
        assertThatThrownBy(() -> couponService.createCoupon(createCouponDto))
                .isInstanceOf(InsufficientPointsException.class);
    }

    @Test
    void shouldGetCouponByIdSuccessfully() {
        // given
        var loyaltyAccountId = createLoyaltyAccountWithPoints(1000);
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
        var accountId = createLoyaltyAccountWithPoints(1000);
        var couponId = createCoupon(accountId, NominalValue.TWENTY);
        var redeemRequest = new RedeemCouponDto(accountId);

        // when
        couponService.redeemCoupon(couponId, redeemRequest);

        // then
        var redeemedCoupon = couponService.getCoupon(couponId);
        assertThat(redeemedCoupon).isNotNull();
        assertThat(redeemedCoupon.id()).isEqualTo(couponId);
        assertThat(redeemedCoupon.isActive()).isFalse();
        assertThat(redeemedCoupon.nominalValue()).isEqualTo(NominalValue.TWENTY);
        assertThat(redeemedCoupon.loyaltyAccountId()).isEqualTo(accountId);
    }

    @Test
    void shouldThrowExceptionWhenRedeemingNonExistentCoupon() {
        // given
        var accountId = createLoyaltyAccountWithPoints(1000);
        var nonExistentCouponId = UUID.randomUUID();
        var redeemRequest = new RedeemCouponDto(accountId);

        // expected
        assertThatThrownBy(() -> couponService.redeemCoupon(nonExistentCouponId, redeemRequest))
                .isInstanceOf(CouponNotFoundException.class);
    }

    @Test
    void shouldThrowExceptionWhenUnauthorizedRedemptionAttempted() {
        // given
        var accountId = createLoyaltyAccountWithPoints(1000);
        var anotherAccountId = createLoyaltyAccountWithPoints(500);
        var couponId = createCoupon(accountId, NominalValue.TWENTY);
        var redeemRequest = new RedeemCouponDto(anotherAccountId);

        // expected
        assertThatThrownBy(() -> couponService.redeemCoupon(couponId, redeemRequest))
                .isInstanceOf(UnauthorizedCouponAccessException.class);
    }

    @Test
    void shouldThrowExceptionWhenRedeemingInactiveCoupon() {
        // given
        var accountId = createLoyaltyAccountWithPoints(1000);
        var couponId = createCoupon(accountId, NominalValue.TWENTY);
        var redeemRequest = new RedeemCouponDto(accountId);
        couponService.redeemCoupon(couponId, redeemRequest);

        // expected
        assertThatThrownBy(() -> couponService.redeemCoupon(couponId, redeemRequest))
                .isInstanceOf(CouponNotActiveException.class);
    }

    private UUID createCoupon(UUID accountId, NominalValue nominalValue) {
        var createCouponDto = new CreateCouponDto(accountId, nominalValue);
        return couponService.createCoupon(createCouponDto);
    }

    private UUID createLoyaltyAccountWithPoints(int points) {
        var customerId = UUID.randomUUID();
        var accountId = loyaltyAccountService.addAccount(new CreateLoyaltyAccountDto(customerId));
        loyaltyAccountService.addPoints(accountId, points);
        return accountId;
    }
}