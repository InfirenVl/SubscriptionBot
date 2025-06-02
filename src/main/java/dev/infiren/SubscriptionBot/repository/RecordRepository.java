package dev.infiren.SubscriptionBot.repository;

import dev.infiren.SubscriptionBot.entity.Record;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RecordRepository extends JpaRepository<Record, Long> {
    Optional<Record> findByCode(String code);
}
