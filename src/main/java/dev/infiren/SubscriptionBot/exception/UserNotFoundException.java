package dev.infiren.SubscriptionBot.exception;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(long id) {
        super("User with id " + id + " was not found");
    }
    public UserNotFoundException(String message) {super(message);}
}
