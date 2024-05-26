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

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CreateCouponAcceptanceTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private FixtureCouponAcceptanceTest fixture;

    @Test
    @DisplayName("""
            given valid coupon creation request,
            when request is sent,
            then coupon is created and HTTP 201 status returned with location header""")
    void givenValidCouponCreationRequest_whenRequestIsSent_thenCouponCreatedAndHttp201Returned() {
        // given
        var intPoints = 1000;
        var loyaltyAccountWithPointsId = fixture.createLoyaltyAccountWithPoints(intPoints);
        var expectedPointsAfterCreateCoupon = intPoints - NominalValue.TWENTY.getRequiredPoints();
        var createCouponDto = new CreateCouponDto(loyaltyAccountWithPointsId, NominalValue.TWENTY);

        //when
        var postResponse = restTemplate.postForEntity(getBaseCouponsUrl(), createCouponDto, Void.class);

        //then
        assertThat(postResponse.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(postResponse.getHeaders().getLocation()).isNotNull();

        var getResponse = restTemplate.getForEntity(postResponse.getHeaders().getLocation(), CouponDto.class);
        assertThat(getResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(getResponse.getBody())
                .isNotNull()
                .hasNoNullFieldsOrProperties()
                .hasFieldOrPropertyWithValue("loyaltyAccountId", createCouponDto.loyaltyAccountId())
                .hasFieldOrPropertyWithValue("nominalValue", createCouponDto.nominalValue())
                .hasFieldOrPropertyWithValue("isActive", true)
                .extracting("id").isNotNull();

        assertThat(fixture.getLoyaltyAccountPoints(loyaltyAccountWithPointsId))
                .isEqualTo(expectedPointsAfterCreateCoupon);
    }

    @Test
    @DisplayName("""
            given request for coupon creation for non-existent loyalty account,
            when request is sent,
            then HTTP 404 status received""")
    void givenRequestToCreateCouponForNonExistentLoyaltyAccount_whenRequestIsSent_thenHttp404Received() {
        // given
        var nonExistentLoyaltyAccountId = UUID.randomUUID();
        var createCouponDto = new CreateCouponDto(nonExistentLoyaltyAccountId, NominalValue.TWENTY);

        // when
        var response = restTemplate.postForEntity(getBaseCouponsUrl(), createCouponDto, ApiErrorResponse.class);

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody())
                .isNotNull()
                .hasNoNullFieldsOrProperties()
                .extracting("message")
                .asString()
                .contains("Could not find loyalty account");
    }

    @Test
    @DisplayName("""
            given loyalty account has insufficient points,
            when coupon creation is attempted,
            then receive HTTP 400 Bad Request""")
    void givenLoyaltyAccountHasInsufficientPoints_whenCouponCreationIsAttempted_thenReceiveHttp400BadRequest() {
        // given
        var intPoints = 40;
        var loyaltyAccountWithPointsId = fixture.createLoyaltyAccountWithPoints(intPoints);
        var createCouponDto = new CreateCouponDto(loyaltyAccountWithPointsId, NominalValue.FIFTY);

        // when
        var response = restTemplate.postForEntity(getBaseCouponsUrl(), createCouponDto, ApiErrorResponse.class);

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody())
                .isNotNull()
                .hasNoNullFieldsOrProperties()
                .extracting("message")
                .asString()
                .contains("Loyalty account doesn't have enough points");

        assertThat(fixture.getLoyaltyAccountPoints(loyaltyAccountWithPointsId))
                .isEqualTo(intPoints);
    }

    String getBaseCouponsUrl() {
        return "http://localhost:" + port + "/coupons";
    }
}