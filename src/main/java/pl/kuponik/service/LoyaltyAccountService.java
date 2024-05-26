package pl.kuponik.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.kuponik.dto.CreateLoyaltyAccountDto;
import pl.kuponik.dto.LoyaltyAccountDto;
import pl.kuponik.exception.InsufficientPointsException;
import pl.kuponik.exception.LoyaltyAccountNotFoundException;
import pl.kuponik.model.LoyaltyAccount;
import pl.kuponik.repostiory.LoyaltyAccountRepository;

import java.util.List;
import java.util.UUID;

@Service
public class LoyaltyAccountService {

    @Autowired
    private LoyaltyAccountRepository loyaltyAccountRepository;

    public LoyaltyAccountDto getAccount(UUID id) {
        return loyaltyAccountRepository.findById(id)
                .map(acc -> new LoyaltyAccountDto(acc.getId(), acc.getCustomerId(), acc.getPoints()))
                .orElseThrow(() -> new LoyaltyAccountNotFoundException(id));
    }

    public List<LoyaltyAccountDto> getAccountByCustomerId(UUID customerId) {
        return loyaltyAccountRepository.findByCustomerId(customerId)
                .stream()
                .map(acc -> new LoyaltyAccountDto(acc.getId(), acc.getCustomerId(), acc.getPoints()))
                .toList();
    }

    public UUID addAccount(CreateLoyaltyAccountDto createDto) {
        var account = new LoyaltyAccount();
        account.setId(UUID.randomUUID());
        account.setCustomerId(createDto.customerId());
        account.setPoints(0);
        return loyaltyAccountRepository.save(account).getId();
    }

    @Transactional
    public void addPoints(UUID id, Integer pointsToAdd) {
        var account = loyaltyAccountRepository.findById(id).orElseThrow(()
                -> new LoyaltyAccountNotFoundException(id));
        account.setPoints(account.getPoints() + pointsToAdd);
        loyaltyAccountRepository.save(account);
    }

    @Transactional
    public void subtractPoints(UUID id, Integer pointsToSubtract) {
        var account = loyaltyAccountRepository.findById(id).orElseThrow(()
                -> new LoyaltyAccountNotFoundException(id));

        if (account.getPoints() < pointsToSubtract) {
            throw new InsufficientPointsException(id);
        }

        account.setPoints(account.getPoints() - pointsToSubtract);
        loyaltyAccountRepository.save(account);
    }
}
