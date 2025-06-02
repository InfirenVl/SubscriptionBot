package dev.infiren.SubscriptionBot.service;

import dev.infiren.SubscriptionBot.entity.Request;
import dev.infiren.SubscriptionBot.entity.User;

import java.util.List;

public interface RequestService {
    void create(String requestText, User user);

    List<Request> getAll();

    Request get(int requestId);

    void update(Request request);

    void delete(int requestId);
}
