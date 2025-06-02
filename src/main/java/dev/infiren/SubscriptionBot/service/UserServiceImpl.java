package dev.infiren.SubscriptionBot.service;

import dev.infiren.SubscriptionBot.entity.User;
import dev.infiren.SubscriptionBot.exception.UserNotFoundException;
import dev.infiren.SubscriptionBot.repository.UserRepository;
import org.springframework.stereotype.Component;
import java.util.List;


@Component
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User get(long id) throws UserNotFoundException {
        return userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(id));
    }
    @Override
    public List<User> getAll() {
        return userRepository.findAll();
    }

    @Override
    public void update(User user) {
        userRepository.save(user);
    }

    @Override
    public void updateStatement(long userId, User.Statement statement) {
        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(userId));
        if (user.getStatement() != statement) {
            user.setStatement(statement);
            userRepository.save(user);
        }
    }




}
