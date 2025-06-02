package dev.infiren.SubscriptionBot.service;

import dev.infiren.SubscriptionBot.entity.Subscription;
import dev.infiren.SubscriptionBot.entity.Record;
import dev.infiren.SubscriptionBot.entity.User;
import dev.infiren.SubscriptionBot.entity.UserRecord;
import dev.infiren.SubscriptionBot.exception.RecordNotFoundException;
import dev.infiren.SubscriptionBot.exception.UserNotFoundException;
import dev.infiren.SubscriptionBot.repository.RecordRepository;
import dev.infiren.SubscriptionBot.repository.UserRecordRepository;
import dev.infiren.SubscriptionBot.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.apache.commons.lang3.RandomStringUtils;
import org.hibernate.Hibernate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class RecordServiceImpl implements RecordService {
    private final RecordRepository repository;
    private final UserRepository userRepository;
    private final UserRecordRepository userRecordRepository;

    public RecordServiceImpl(RecordRepository repository, UserRepository userRepository, UserRecordRepository userRecordRepository) {
        this.repository = repository;
        this.userRepository = userRepository;
        this.userRecordRepository = userRecordRepository;
    }

    @Transactional
    @Override
    public void create(long userId, Subscription subscription) {
        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(userId));
        Record newRecord = new Record();
        newRecord.setSubscriptionType(subscription);
        newRecord.setDateOfPayment(LocalDateTime.now().plusMonths(1));
        newRecord.setCreator(user);
        if(subscription.getMaxMembers() > 1)
            newRecord.setCode(generateCode());
        else
            newRecord.setCode(null);
        repository.save(newRecord);
        userRecordRepository.save(new UserRecord(user, newRecord, true));
    }
    @Transactional
    @Override
    public Record getByIdWithUserRecords(long id) {
        Record record = repository.findById(id).orElseThrow(() -> new RecordNotFoundException(id));
        Hibernate.initialize(record.getUserRecords());
        return record;
    }
    @Override
    public Record getById(long id) {
        return repository.findById(id).orElseThrow(() -> new RecordNotFoundException(id));
    }
    @Override
    public Record getByCode(String code) {
        return repository.findByCode(code).orElseThrow(() -> new RecordNotFoundException("Код неверен"));
    }
    @Override
    public List<Record> getAll() {
       return repository.findAll();
    }
    @Override
    public void delete(Record record) {
        repository.delete(record);
    }
    @Override
    public void updateCode(long recordId) {
        Record record = repository.findById(recordId).orElseThrow(() -> new RecordNotFoundException(recordId));
        String newCode = generateCode();
        while (repository.findByCode(newCode).isPresent()) {
            newCode = generateCode();
        }
        record.setCode(newCode);
        repository.save(record);
    }
    private String generateCode() {
        return RandomStringUtils.random(20, true, true);
    }

    @Override
    public void update(Record record) {
        repository.save(record);
    }
}
