package pl.kuponik.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.kuponik.dto.CreateLoyaltyAccountDto;
import pl.kuponik.dto.LoyaltyAccountDto;
import pl.kuponik.exception.LoyaltyAccountNotFoundException;
import pl.kuponik.model.LoyaltyAccount;
import pl.kuponik.model.LoyaltyPoints;
import pl.kuponik.repostiory.LoyaltyAccountRepository;

import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class LoyaltyAccountService {

    private final LoyaltyAccountRepository loyaltyAccountRepository;

    public LoyaltyAccountDto getAccount(UUID id) {
        return loyaltyAccountRepository.findById(id)
                .map(acc -> new LoyaltyAccountDto(acc.getId(), acc.getCustomerId(), acc.getPoints().points()))
                .orElseThrow(() -> new LoyaltyAccountNotFoundException(id));
    }

    public List<LoyaltyAccountDto> getAccountByCustomerId(UUID customerId) {
        return loyaltyAccountRepository.findByCustomerId(customerId)
                .stream()
                .map(acc -> new LoyaltyAccountDto(acc.getId(), acc.getCustomerId(), acc.getPoints().points()))
                .toList();
    }

    public UUID addAccount(CreateLoyaltyAccountDto createDto) {
        var account = new LoyaltyAccount(createDto.customerId());
        return loyaltyAccountRepository.save(account).getId();
    }

    @Transactional
    public void addPoints(UUID id, Integer pointsToAdd) {
        var account = loyaltyAccountRepository.findById(id).orElseThrow(()
                -> new LoyaltyAccountNotFoundException(id));

        account.addPoints(new LoyaltyPoints(pointsToAdd));
        loyaltyAccountRepository.save(account);
    }

    @Transactional
    public void subtractPoints(UUID id, Integer pointsToSubtract) {
        var account = loyaltyAccountRepository.findById(id).orElseThrow(()
                -> new LoyaltyAccountNotFoundException(id));

        account.subtractPoints(new LoyaltyPoints(pointsToSubtract));
        loyaltyAccountRepository.save(account);
    }
}
