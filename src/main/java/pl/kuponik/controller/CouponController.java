package pl.kuponik.controller;


import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import pl.kuponik.dto.CouponDto;
import pl.kuponik.dto.CreateCouponDto;
import pl.kuponik.dto.RedeemCouponRequest;
import pl.kuponik.service.CouponService;

import java.net.URI;
import java.util.UUID;

@RestController
@RequestMapping("/coupons")
@AllArgsConstructor
public class CouponController {

    private final CouponService couponService;

    @PostMapping
    public ResponseEntity<Void> createCoupon(@Valid @RequestBody CreateCouponDto request) {
        UUID id = couponService.createCoupon(request);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(id)
                .toUri();

        return ResponseEntity.created(location).build();
    }

    @PutMapping("/{id}/redeem")
    public ResponseEntity<Void> redeemCoupon(@PathVariable UUID id, @Valid @RequestBody RedeemCouponRequest request) {
        couponService.redeemCoupon(id, request);

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<CouponDto> getCoupon(@PathVariable UUID id) {
        var couponDto = couponService.getCoupon(id);

        return ResponseEntity.ok(couponDto);
    }
}
