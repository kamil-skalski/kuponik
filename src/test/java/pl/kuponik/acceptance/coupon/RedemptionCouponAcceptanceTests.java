package pl.kuponik.acceptance.coupon;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import pl.kuponik.controller.ApiErrorResponse;
import pl.kuponik.dto.CreateCouponDto;
import pl.kuponik.dto.RedeemCouponRequest;
import pl.kuponik.model.NominalValue;
import pl.kuponik.service.CouponService;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class RedemptionCouponAcceptanceTests {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private CouponService couponService;

    @Autowired
    private FixtureCouponAcceptanceTest fixture;

    @Test
    @DisplayName("""
            given request to redeem coupon for valid coupon,
            when request is sent,
            then HTTP 204 status received""")
    void givenRequestToRedeemValidCoupon_whenRequestIsSent_thenHttp204Received() {
        //given
        var loyaltyAccountId = fixture.createLoyaltyAccountWithPoints();
        var couponId = couponService.createCoupon(new CreateCouponDto(loyaltyAccountId, NominalValue.TWENTY));
        var redeemCouponRequest = new RedeemCouponRequest(loyaltyAccountId);

        // when
        var response = restTemplate.exchange(
                getBaseCouponsUrl() + "/" + couponId + "/redeem",
                HttpMethod.PUT,
                new HttpEntity<>(redeemCouponRequest),
                Void.class);

        //then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
        var coupon = couponService.getCoupon(couponId);
        assertThat(coupon)
                .isNotNull()
                .hasNoNullFieldsOrProperties()
                .hasFieldOrPropertyWithValue("id", couponId)
                .hasFieldOrPropertyWithValue("loyaltyAccountId", loyaltyAccountId)
                .hasFieldOrPropertyWithValue("nominalValue", NominalValue.TWENTY)
                .hasFieldOrPropertyWithValue("active", false);
    }

    @Test
    @DisplayName("""
            given request to redeem coupon for non-existent coupon,
            when request is sent,
            then HTTP 404 status received""")
    void givenRequestToRedeemCouponForNonExistentCoupon_whenRequestIsSent_thenHttp404Received() {
        //given
        var loyaltyAccountId = fixture.createLoyaltyAccountWithPoints();
        var redeemCouponRequest = new RedeemCouponRequest(loyaltyAccountId);
        var nonExistentCouponId = UUID.randomUUID();

        // when
        var response = restTemplate.exchange(
                getBaseCouponsUrl() + "/" + nonExistentCouponId + "/redeem",
                HttpMethod.PUT,
                new HttpEntity<>(redeemCouponRequest),
                ApiErrorResponse.class);

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody())
                .isNotNull()
                .hasNoNullFieldsOrProperties()
                .extracting("message")
                .asString()
                .contains("Coupon not found");
    }

    @Test
    @DisplayName("""
            given coupon is not active,
            when redeem attempt is made,
            then receive HTTP 409 Conflict""")
    void givenCouponIsNotActive_whenRedeemAttemptIsMade_thenReceiveHttp409Conflict() {
        //given
        var loyaltyAccountId = fixture.createLoyaltyAccountWithPoints();
        var couponId = couponService.createCoupon(new CreateCouponDto(loyaltyAccountId, NominalValue.TWENTY));
        var redeemCouponRequest = new RedeemCouponRequest(loyaltyAccountId);
        couponService.redeemCoupon(couponId, redeemCouponRequest);

        // when
        var response = restTemplate.exchange(
                getBaseCouponsUrl() + "/" + couponId + "/redeem",
                HttpMethod.PUT,
                new HttpEntity<>(redeemCouponRequest),
                ApiErrorResponse.class);

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
        assertThat(response.getBody())
                .isNotNull()
                .hasNoNullFieldsOrProperties()
                .extracting("message")
                .asString()
                .contains("Coupon with ID: " + couponId + " is not active");
    }

    @Test
    @DisplayName("""
            given coupon does not belong to the loyalty account,
            when redeem attempt is made,
            then receive HTTP 403 Forbidden""")
    void givenCouponDoesNotBelongToLoyaltyAccount_whenRedeemAttemptIsMade_thenReceiveHttp403Forbidden() {
        //given
        var ownerLoyaltyAccountId = fixture.createLoyaltyAccountWithPoints();
        var nonOwnerLoyaltyAccountId = fixture.createLoyaltyAccountWithPoints();
        var couponId = couponService.createCoupon(new CreateCouponDto(ownerLoyaltyAccountId, NominalValue.TWENTY));
        var redeemCouponRequest = new RedeemCouponRequest(nonOwnerLoyaltyAccountId);

        // when
        var response = restTemplate.exchange(
                getBaseCouponsUrl() + "/" + couponId + "/redeem",
                HttpMethod.PUT,
                new HttpEntity<>(redeemCouponRequest),
                ApiErrorResponse.class);

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
        assertThat(response.getBody())
                .isNotNull()
                .hasNoNullFieldsOrProperties()
                .extracting("message")
                .asString()
                .contains("This coupon with ID: " + couponId + " does not belong to the loyalty account");
    }

    String getBaseCouponsUrl() {
        return "http://localhost:" + port + "/coupons";
    }
}
