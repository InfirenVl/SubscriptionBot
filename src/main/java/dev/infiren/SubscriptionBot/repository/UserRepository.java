package dev.infiren.SubscriptionBot.repository;

import dev.infiren.SubscriptionBot.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findById(long id);
}
