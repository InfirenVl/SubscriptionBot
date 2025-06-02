package dev.infiren.SubscriptionBot.service;

import dev.infiren.SubscriptionBot.entity.Subscription;
import dev.infiren.SubscriptionBot.exception.RecordNotFoundException;
import dev.infiren.SubscriptionBot.repository.SubscriptionRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SubscriptionServiceImpl implements SubscriptionService {
    private final SubscriptionRepository subscriptionRepository;

    public SubscriptionServiceImpl(SubscriptionRepository subscriptionRepository) {
        this.subscriptionRepository = subscriptionRepository;
    }

    @Override
    public List<Subscription> getAllSubscriptions() {
        return subscriptionRepository.findAll();
    }
    @Override
    public Subscription getSubscription(int id) throws RecordNotFoundException {
        return subscriptionRepository.findById(id).orElseThrow(() -> new RecordNotFoundException(id));
    }
    @Override
    public void create(Subscription subscription) {
        subscriptionRepository.save(subscription);
    }
}
