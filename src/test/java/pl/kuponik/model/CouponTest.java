package pl.kuponik.model;

import org.junit.jupiter.api.Test;
import pl.kuponik.domain.exception.CouponNotActiveException;
import pl.kuponik.domain.Coupon;
import pl.kuponik.domain.LoyaltyAccount;
import pl.kuponik.domain.NominalValue;
import pl.kuponik.domain.exception.UnauthorizedCouponAccessException;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class CouponTest {

    private final LoyaltyAccount loyaltyAccount = new LoyaltyAccount(UUID.randomUUID());

    @Test
    void shouldCreateCoupon() {
        var nominalValue = NominalValue.TWENTY;
        var coupon = new Coupon(loyaltyAccount, nominalValue);

        assertThat(coupon.getId()).isNotNull();
        assertThat(coupon.getLoyaltyAccount()).isEqualTo(loyaltyAccount);
        assertThat(coupon.getNominalValue()).isEqualTo(nominalValue);
        assertThat(coupon.isActive()).isTrue();
    }

    @Test
    void shouldRedeemCoupon() {
        var coupon = new Coupon(loyaltyAccount, NominalValue.TWENTY);

        coupon.redeem(loyaltyAccount.getId());

        assertThat(coupon.getLoyaltyAccount()).isEqualTo(loyaltyAccount);
        assertThat(coupon.getNominalValue()).isEqualTo(NominalValue.TWENTY);
        assertThat(coupon.isActive()).isFalse();
    }

    @Test
    void shouldThrowUnauthorizedCouponAccessExceptionWhenRedeemedByNotOwner() {
        var coupon = new Coupon(loyaltyAccount, NominalValue.TWENTY);
        var notOwnerId = UUID.randomUUID();

        assertThrows(UnauthorizedCouponAccessException.class,
                () -> coupon.redeem(notOwnerId));
    }

    @Test
    void shouldThrowCouponNotActiveExceptionWhenRedeemingAnInactiveCoupon() {
        var coupon = new Coupon(loyaltyAccount, NominalValue.TWENTY);
        var loyaltyAccountID = loyaltyAccount.getId();
        coupon.redeem(loyaltyAccountID);

        assertThrows(CouponNotActiveException.class,
                () -> coupon.redeem(loyaltyAccountID));
    }
}