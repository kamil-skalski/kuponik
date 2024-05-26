package pl.kuponik.loyalty.exception;


import java.util.UUID;

public class InsufficientPointsException extends RuntimeException {

    public InsufficientPointsException(UUID accountId) {
        super("Loyalty account doesn't have enough points. Id: " + accountId);
    }
}