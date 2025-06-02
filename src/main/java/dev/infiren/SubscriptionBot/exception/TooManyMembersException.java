package dev.infiren.SubscriptionBot.exception;

public class TooManyMembersException extends Exception {
    public TooManyMembersException() {
        super("В подписке уже максимальное количество пользователей");
    }
}
