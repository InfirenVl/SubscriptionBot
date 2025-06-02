package dev.infiren.SubscriptionBot.exception;

public class RecordNotFoundException extends RuntimeException {
    public RecordNotFoundException(long id){ super("Subscription with id "+ id +" was not found");}
    public RecordNotFoundException(String message) {
        super(message);
    }
}
