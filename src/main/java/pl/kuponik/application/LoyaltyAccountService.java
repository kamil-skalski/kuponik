package pl.kuponik.application;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.kuponik.domain.LoyaltyAccount;
import pl.kuponik.domain.LoyaltyPoints;
import pl.kuponik.application.exception.LoyaltyAccountNotFoundException;
import pl.kuponik.domain.repostiory.LoyaltyAccountRepository;
import pl.kuponik.application.dto.CreateLoyaltyAccountDto;
import pl.kuponik.application.dto.LoyaltyAccountDto;

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
    public void addPoints(UUID id, int pointsToAdd) {
        var account = loyaltyAccountRepository.findById(id).orElseThrow(()
                -> new LoyaltyAccountNotFoundException(id));

        account.addPoints(new LoyaltyPoints(pointsToAdd));
        loyaltyAccountRepository.save(account);
    }

    @Transactional
    public void subtractPoints(UUID id, int pointsToSubtract) {
        var account = loyaltyAccountRepository.findById(id).orElseThrow(()
                -> new LoyaltyAccountNotFoundException(id));

        account.subtractPoints(new LoyaltyPoints(pointsToSubtract));
        loyaltyAccountRepository.save(account);
    }
}
