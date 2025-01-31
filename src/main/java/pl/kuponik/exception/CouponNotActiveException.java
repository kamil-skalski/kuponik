package pl.kuponik.exception;

import java.util.UUID;

public class CouponNotActiveException extends RuntimeException {

    public CouponNotActiveException(UUID couponId) {
        super("Coupon with ID: " + couponId + " is not active.");
    }
}