package pl.kuponik.exception;

import java.util.UUID;

public class UnauthorizedCouponAccessException extends RuntimeException {

    public UnauthorizedCouponAccessException(UUID couponId, UUID loyaltyAccountId) {
        super("This coupon with ID: " + couponId + " does not belong to the loyalty accountId with ID: " + loyaltyAccountId + ".");
    }
}