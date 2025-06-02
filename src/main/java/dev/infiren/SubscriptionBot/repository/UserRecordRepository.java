package dev.infiren.SubscriptionBot.repository;

import dev.infiren.SubscriptionBot.entity.Record;
import dev.infiren.SubscriptionBot.entity.User;
import dev.infiren.SubscriptionBot.entity.UserRecord;
import dev.infiren.SubscriptionBot.entity.UserRecordId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRecordRepository extends JpaRepository<UserRecord, UserRecordId> {
    List<UserRecord> findAllByUserAndIsActive(User user, boolean isActive);
    UserRecord findByUserAndRecord(User user, Record record);
}
