package dev.infiren.SubscriptionBot.repository;

import dev.infiren.SubscriptionBot.entity.Request;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RequestRepository extends JpaRepository<Request, Integer> {
}
