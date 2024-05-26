package pl.kuponik.controller;


import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import pl.kuponik.dto.CreateLoyaltyAccountDto;
import pl.kuponik.dto.LoyaltyAccountDto;
import pl.kuponik.dto.ModifyPointsRequest;
import pl.kuponik.service.LoyaltyAccountService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/loyalty-accounts")
@AllArgsConstructor
public class LoyaltyAccountController {

    private final LoyaltyAccountService loyaltyAccountService;

    @PostMapping
    public ResponseEntity<Void> createLoyaltyAccount(@Valid @RequestBody CreateLoyaltyAccountDto request) {
        var id = loyaltyAccountService.addAccount(request);

        var location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(id)
                .toUri();

        return ResponseEntity.created(location).build();
    }

    @PutMapping("/{id}/add-points")
    public ResponseEntity<Void> addPoints(@PathVariable UUID id, @Valid @RequestBody ModifyPointsRequest dto) {
        loyaltyAccountService.addPoints(id, dto.points());

        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}/subtract-points")
    public ResponseEntity<Void> subtractPoints(@PathVariable UUID id, @Valid @RequestBody ModifyPointsRequest dto) {
        loyaltyAccountService.subtractPoints(id, dto.points());

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<LoyaltyAccountDto> getLoyaltyAccount(@PathVariable UUID id) {
        var dto = loyaltyAccountService.getAccount(id);

        return ResponseEntity.ok(dto);
    }

    @GetMapping
    public ResponseEntity<List<LoyaltyAccountDto>> getLoyaltyAccountsByCustomerId(@RequestParam UUID customerId) {
        var accounts = loyaltyAccountService.getAccountByCustomerId(customerId);

        return ResponseEntity.ok(accounts);
    }
}
