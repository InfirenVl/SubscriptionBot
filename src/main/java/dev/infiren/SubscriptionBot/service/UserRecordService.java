package dev.infiren.SubscriptionBot.service;

import dev.infiren.SubscriptionBot.entity.Record;
import dev.infiren.SubscriptionBot.entity.User;
import dev.infiren.SubscriptionBot.exception.RecordNotFoundException;
import dev.infiren.SubscriptionBot.exception.TooManyMembersException;
import dev.infiren.SubscriptionBot.exception.UserNotFoundException;
import jakarta.transaction.Transactional;

import java.util.List;

public interface UserRecordService {
    void create(User user, Record record);

    @Transactional
    void createByCode(String code, long userId) throws RecordNotFoundException, TooManyMembersException, UserNotFoundException;

    @Transactional
    List<Record> getAllRecordsByUserAndActive(long userId, boolean isActive);

    @Transactional
    List<Record> getAllRecordsByUser(long userId) throws UserNotFoundException;

    @Transactional
    List<User> getAllUsersByRecord(long recordId) throws RecordNotFoundException;

    @Transactional
    void delete(long userId, long recordId) throws UserNotFoundException, RecordNotFoundException;

    void changeStatus(long userId, long recordId);
}
