package pl.kuponik.infrastructure.api;


import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import pl.kuponik.application.CouponService;
import pl.kuponik.application.dto.CreateCouponDto;
import pl.kuponik.application.dto.RedeemCouponDto;
import pl.kuponik.domain.NominalValue;
import pl.kuponik.infrastructure.api.dto.CouponResponse;
import pl.kuponik.infrastructure.api.dto.CreateCouponRequest;
import pl.kuponik.infrastructure.api.dto.NominalValueApi;
import pl.kuponik.infrastructure.api.dto.RedeemCouponRequest;

import java.net.URI;
import java.util.UUID;

@RestController
@RequestMapping("/coupons")
@AllArgsConstructor
public class CouponController {

    private final CouponService couponService;

    @PostMapping
    public ResponseEntity<Void> createCoupon(@Valid @RequestBody CreateCouponRequest request) {
        UUID id = couponService.createCoupon(new CreateCouponDto(request.loyaltyAccountId(),
                NominalValue.valueOf(request.nominalValue().name())));

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(id)
                .toUri();

        return ResponseEntity.created(location).build();
    }

    @PutMapping("/{id}/redeem")
    public ResponseEntity<Void> redeemCoupon(@PathVariable UUID id, @Valid @RequestBody RedeemCouponRequest request) {
        couponService.redeemCoupon(id, new RedeemCouponDto(request.loyaltyAccountId()));

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<CouponResponse> getCoupon(@PathVariable UUID id) {
        var couponDto = couponService.getCoupon(id);
        var response = new CouponResponse(couponDto.id(), couponDto.loyaltyAccountId(),
                NominalValueApi.valueOf(couponDto.nominalValue().name()), couponDto.isActive());

        return ResponseEntity.ok(response);
    }
}
