package pl.kuponik.model;

import org.junit.jupiter.api.Test;
import pl.kuponik.exception.InsufficientPointsException;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class LoyaltyAccountTest {
    private final UUID customerId = UUID.randomUUID();


    @Test
    void shouldCreateLoyaltyAccount() {
        var account = new LoyaltyAccount(customerId);

        assertThat(account.getId()).isNotNull();
        assertThat(account.getCustomerId()).isEqualTo(customerId);
        assertThat(account.getPoints()).isZero();
    }

    @Test
    void shouldAddPoints() {
        var account = new LoyaltyAccount(customerId);
        var pointsToAdd = 100;

        account.addPoints(pointsToAdd);
        assertEquals(pointsToAdd, account.getPoints());

        account.addPoints(pointsToAdd);
        assertEquals(pointsToAdd + pointsToAdd, account.getPoints());
    }

    @Test
    void shouldSubtractPoints() {
        var account = new LoyaltyAccount(customerId);
        var initPoints = 100;
        account.addPoints(initPoints);
        var pointsToSubtract = 40;

        account.subtractPoints(pointsToSubtract);
        assertEquals(initPoints - pointsToSubtract, account.getPoints());

        account.subtractPoints(pointsToSubtract);
        assertEquals(initPoints - pointsToSubtract - pointsToSubtract, account.getPoints());
    }

    @Test
    void shouldThrowInsufficientPointsExceptionWhenSubtractingMorePointsThanExist() {
        var account = new LoyaltyAccount(customerId);
        var initPoints = 100;
        account.addPoints(initPoints);
        var pointsToSubtract = 101;

        assertThrows(InsufficientPointsException.class,
                () -> account.subtractPoints(pointsToSubtract));

        assertEquals(initPoints, account.getPoints());

    }
}