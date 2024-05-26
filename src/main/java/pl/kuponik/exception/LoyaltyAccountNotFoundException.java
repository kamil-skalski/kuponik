package pl.kuponik.exception;

import java.util.UUID;

public class LoyaltyAccountNotFoundException extends RuntimeException {

    public LoyaltyAccountNotFoundException(final UUID id) {
        super("Could not find loyalty account with id: " + id);
    }
}
