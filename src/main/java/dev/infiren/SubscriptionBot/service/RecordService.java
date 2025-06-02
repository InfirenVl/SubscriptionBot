package dev.infiren.SubscriptionBot.service;

import dev.infiren.SubscriptionBot.entity.Record;
import dev.infiren.SubscriptionBot.entity.Subscription;
import jakarta.transaction.Transactional;

import java.util.List;

public interface RecordService {
    @Transactional
    void create(long userId, Subscription subscription);

    @Transactional
    Record getByIdWithUserRecords(long id);

    Record getById(long id);

    Record getByCode(String code);

    List<Record> getAll();

    void delete(Record record);

    void updateCode(long recordId);

    void update(Record record);
}
