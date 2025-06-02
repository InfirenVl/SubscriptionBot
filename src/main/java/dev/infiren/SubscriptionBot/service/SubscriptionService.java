package dev.infiren.SubscriptionBot.service;

import dev.infiren.SubscriptionBot.entity.Subscription;
import dev.infiren.SubscriptionBot.exception.RecordNotFoundException;

import java.util.List;

public interface SubscriptionService {
    List<Subscription> getAllSubscriptions();

    Subscription getSubscription(int id) throws RecordNotFoundException;

    void create(Subscription subscription);
}
