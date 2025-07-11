package dev.infiren.SubscriptionBot.repository;

import dev.infiren.SubscriptionBot.entity.Subscription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SubscriptionRepository extends JpaRepository<Subscription, Integer> {
}
