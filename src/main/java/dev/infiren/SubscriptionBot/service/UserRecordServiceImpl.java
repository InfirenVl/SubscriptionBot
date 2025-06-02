package dev.infiren.SubscriptionBot.service;

import dev.infiren.SubscriptionBot.entity.Record;
import dev.infiren.SubscriptionBot.entity.User;
import dev.infiren.SubscriptionBot.entity.UserRecord;
import dev.infiren.SubscriptionBot.entity.UserRecordId;
import dev.infiren.SubscriptionBot.exception.RecordNotFoundException;
import dev.infiren.SubscriptionBot.exception.TooManyMembersException;
import dev.infiren.SubscriptionBot.exception.UserNotFoundException;
import dev.infiren.SubscriptionBot.repository.UserRecordRepository;
import jakarta.transaction.Transactional;
import org.hibernate.Hibernate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserRecordServiceImpl implements UserRecordService {
    private final RecordService recordService;
    private final UserService userService;
    private final UserRecordRepository repository;
    public UserRecordServiceImpl(RecordService recordService, UserService userService, UserRecordRepository repository) {
        this.recordService = recordService;
        this.userService = userService;
        this.repository = repository;
    }

    @Override
    public void create(User user, Record record) {
        UserRecord userRecord = new UserRecord(user, record, true);
        repository.save(userRecord);
    }
    @Transactional
    @Override
    public void createByCode(String code, long userId) throws RecordNotFoundException, TooManyMembersException, UserNotFoundException {
        Record record = recordService.getByCode(code);
        if(record.getUserRecords().size() < record.getSubscriptionType().getMaxMembers()){
            User user = userService.get(userId);
            repository.save(new UserRecord(user, record, true));
        }
        else
            throw new TooManyMembersException();
    }
    @Transactional
    @Override
    public List<Record> getAllRecordsByUserAndActive(long userId, boolean isActive) {
        User user = this.userService.get(userId);
        List<UserRecord> userRecords = this.repository.findAllByUserAndIsActive(user, isActive);
        List<Record> finalList = new ArrayList();
        for(UserRecord record : userRecords) {
            Hibernate.initialize(record.getRecord().getUserRecords());
            finalList.add(record.getRecord());
        }
        return finalList;
    }
    @Transactional
    @Override
    public List<Record> getAllRecordsByUser(long userId) throws UserNotFoundException {
        User user = userService.get(userId);
        Hibernate.initialize(user.getUserRecords());
        List<Record> recordList = new ArrayList<>();
        for(UserRecord record : user.getUserRecords()) {
            recordList.add(record.getRecord());
        }
        return recordList;
    }
    @Transactional
    @Override
    public List<User> getAllUsersByRecord(long recordId) throws RecordNotFoundException {
        Record record = recordService.getById(recordId);
        Hibernate.initialize(record.getUserRecords());
        List<User> userList = new ArrayList<>();
        for(UserRecord userRecord : record.getUserRecords()) {
            userList.add(userRecord.getUser());
        }
        return userList;
    }

    @Transactional
    @Override
    public void delete(long userId, long recordId) throws UserNotFoundException, RecordNotFoundException {
        User user = userService.get(userId);
        Record record = recordService.getById(recordId);
        repository.deleteById(new UserRecordId(user, record));
        if(record.getUserRecords().size() == 1)
            recordService.delete(record);
    }
    @Override
    public void changeStatus(long userId, long recordId) {
        User user = userService.get(userId);
        Record record = recordService.getById(recordId);
        UserRecord userRecord = repository.findByUserAndRecord(user, record);
        userRecord.setActive(!userRecord.isActive());
        repository.save(userRecord);
    }
}
