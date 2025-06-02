package dev.infiren.SubscriptionBot.exception;

public class RequestNotFoundException extends RuntimeException {
    public RequestNotFoundException(String message) {
        super(message);
    }
    public RequestNotFoundException(int id) {
        super("Request with id" + id + "not found");
    }
}
