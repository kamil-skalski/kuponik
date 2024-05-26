package pl.kuponik.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import pl.kuponik.dto.CreateLoyaltyAccountDto;
import pl.kuponik.dto.LoyaltyAccountDto;
import pl.kuponik.exception.InsufficientPointsException;
import pl.kuponik.exception.LoyaltyAccountNotFoundException;
import pl.kuponik.repostiory.LoyaltyAccountRepository;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

@SpringBootTest
class LoyaltyAccountServiceTest {

    @Autowired
    private LoyaltyAccountService loyaltyAccountService;

    @Autowired
    private LoyaltyAccountRepository loyaltyAccountRepository;

    @AfterEach
    void cleanUp() {
        loyaltyAccountRepository.deleteAll();
    }

    @Test
    void shouldAddAccount() {
        // given
        var customerId = UUID.randomUUID();
        var createDto = new CreateLoyaltyAccountDto(customerId);

        // when
        var accountId = loyaltyAccountService.addAccount(createDto);

        // then
        var savedAccount = loyaltyAccountRepository.findById(accountId).orElseThrow();
        assertThat(savedAccount).isNotNull();
        assertThat(savedAccount.getCustomerId()).isEqualTo(customerId);
        assertThat(savedAccount.getPoints()).isZero();
        assertThat(savedAccount.getId()).isEqualTo(accountId);
    }

    @Test
    void shouldGetAccountById() {
        // given
        var customerId = UUID.randomUUID();
        var accountId = createLoyaltyAccount(customerId);

        // when
        var retrievedAccount = loyaltyAccountService.getAccount(accountId);

        // then
        assertThat(retrievedAccount).isNotNull();
        assertThat(retrievedAccount.customerId()).isEqualTo(customerId);
        assertThat(retrievedAccount.points()).isZero();
        assertThat(retrievedAccount.id()).isEqualTo(accountId);
    }

    @Test
    void shouldThrowExceptionWhenAccountNotFound() {
        // given
        var nonExistentAccountId = UUID.randomUUID();

        // expected
        assertThatThrownBy(() -> loyaltyAccountService.getAccount(nonExistentAccountId))
                .isInstanceOf(LoyaltyAccountNotFoundException.class);
    }

    @Test
    void shouldGetAccountsByCustomerId() {
        // given
        var customerId = UUID.randomUUID();
        var expectedAccountId1 = createLoyaltyAccount(customerId);
        var expectedAccountId2 = createLoyaltyAccount(customerId);

        var nonExpectedAccountId1 = createLoyaltyAccount(UUID.randomUUID());
        var nonExpectedAccountId2 = createLoyaltyAccount(UUID.randomUUID());

        // when
        var accounts = loyaltyAccountService.getAccountByCustomerId(customerId);

        // then
        assertThat(accounts).hasSize(2);
        assertThat(accounts).extracting(LoyaltyAccountDto::id)
                .containsExactlyInAnyOrder(expectedAccountId1, expectedAccountId2)
                .doesNotContain(nonExpectedAccountId1, nonExpectedAccountId2);
    }

    @Test
    void shouldAddPoints() {
        // given
        var accountId = createLoyaltyAccount();
        var pointsToAdd = 100;

        // when
        loyaltyAccountService.addPoints(accountId, pointsToAdd);

        // then
        assertThat(loyaltyAccountService.getAccount(accountId).points()).isEqualTo(pointsToAdd);

        // when
        loyaltyAccountService.addPoints(accountId, pointsToAdd);

        // then
        assertThat(loyaltyAccountService.getAccount(accountId).points()).isEqualTo(pointsToAdd + pointsToAdd);
    }

    @Test
    void shouldThrowExceptionOnAddWhenAccountNotFound() {
        // given
        var nonExistentAccountId = UUID.randomUUID();

        // expected
        assertThatThrownBy(() -> loyaltyAccountService.addPoints(nonExistentAccountId, 100))
                .isInstanceOf(LoyaltyAccountNotFoundException.class);
    }

    @Test
    void shouldSubtractPoints() {
        // given
        var accountId = createLoyaltyAccount();
        var initPoints = 100;
        loyaltyAccountService.addPoints(accountId, initPoints);
        var pointsToSubtract = 20;

        // when
        loyaltyAccountService.subtractPoints(accountId, pointsToSubtract);

        // then
        assertThat(loyaltyAccountService.getAccount(accountId).points()).isEqualTo(initPoints - pointsToSubtract);

        // when
        loyaltyAccountService.subtractPoints(accountId, pointsToSubtract);

        // then
        assertThat(loyaltyAccountService.getAccount(accountId).points()).isEqualTo(initPoints - pointsToSubtract - pointsToSubtract);

    }

    @Test
    void shouldThrowExceptionOnSubtractWhenAccountNotFound() {
        // given
        var nonExistentAccountId = UUID.randomUUID();

        // expected
        assertThatThrownBy(() -> loyaltyAccountService.subtractPoints(nonExistentAccountId, 100))
                .isInstanceOf(LoyaltyAccountNotFoundException.class);
    }

    @Test
    void shouldThrowExceptionOnSubtractWhenAccountNotHaveEnoughPoints() {
        // given
        var accountId = createLoyaltyAccount();
        var initPoints = 100;
        loyaltyAccountService.addPoints(accountId, initPoints);
        var pointsToSubtract = 200;

        // expected
        assertThatThrownBy(() -> loyaltyAccountService.subtractPoints(accountId, pointsToSubtract))
                .isInstanceOf(InsufficientPointsException.class);
    }

    private UUID createLoyaltyAccount(UUID customerId) {
        var createDto = new CreateLoyaltyAccountDto(customerId);
        return loyaltyAccountService.addAccount(createDto);
    }

    private UUID createLoyaltyAccount() {
        return createLoyaltyAccount(UUID.randomUUID());
    }
}
