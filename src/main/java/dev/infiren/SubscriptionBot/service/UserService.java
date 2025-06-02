package dev.infiren.SubscriptionBot.service;

import dev.infiren.SubscriptionBot.entity.User;
import dev.infiren.SubscriptionBot.exception.UserNotFoundException;

import java.util.List;

public interface UserService {
    User get(long id) throws UserNotFoundException;

    List<User> getAll();

    void update(User user);

    void updateStatement(long userId, User.Statement statement);
}
