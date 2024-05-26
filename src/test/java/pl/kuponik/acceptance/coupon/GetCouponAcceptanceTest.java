package pl.kuponik.acceptance.coupon;


import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import pl.kuponik.controller.ApiErrorResponse;
import pl.kuponik.dto.CouponDto;
import pl.kuponik.dto.CreateCouponDto;
import pl.kuponik.model.NominalValue;
import pl.kuponik.service.CouponService;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class GetCouponAcceptanceTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private FixtureCouponAcceptanceTest fixture;

    @Autowired
    private CouponService couponService;

    @Test
    @DisplayName("""
            given existing coupon id,
            when request is sent,
            then return coupon details and HTTP 200 status""")
    void givenExistingCouponId_whenRequestIsSent_thenCouponDetailsReturnedAndHttp200() {
        //given
        var loyaltyAccountId = fixture.createLoyaltyAccountWithPoints();
        var couponId = couponService.createCoupon(new CreateCouponDto(loyaltyAccountId, NominalValue.TWENTY));

        //when
        var response = restTemplate.getForEntity(
                getBaseCouponsUrl() + "/" + couponId, CouponDto.class);

        //then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody())
                .isNotNull()
                .hasNoNullFieldsOrProperties()
                .hasFieldOrPropertyWithValue("id", couponId)
                .hasFieldOrPropertyWithValue("loyaltyAccountId", loyaltyAccountId)
                .hasFieldOrPropertyWithValue("nominalValue", NominalValue.TWENTY)
                .hasFieldOrPropertyWithValue("isActive", true);
    }

    @Test
    @DisplayName("""
            given non-existing coupon id,
            when request is sent,
            then do not return coupon details and HTTP 404 status""")
    void givenNonExistingCouponId_whenRequestIsSent_thenCouponDetailsNotReturnedAndHttp404() {
        //given
        var notExistingId = UUID.randomUUID();

        //when
        var response = restTemplate.getForEntity(
                getBaseCouponsUrl() + "/" + notExistingId, ApiErrorResponse.class);

        //then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody())
                .isNotNull()
                .hasNoNullFieldsOrProperties()
                .extracting("message")
                .asString()
                .contains("Coupon not found");
    }

    String getBaseCouponsUrl() {
        return "http://localhost:" + port + "/coupons";
    }
}