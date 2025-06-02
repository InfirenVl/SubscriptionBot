package dev.infiren.SubscriptionBot.service;

import dev.infiren.SubscriptionBot.entity.Request;
import dev.infiren.SubscriptionBot.entity.User;
import dev.infiren.SubscriptionBot.exception.RequestNotFoundException;
import dev.infiren.SubscriptionBot.repository.RequestRepository;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RequestServiceImpl implements RequestService {
    private final RequestRepository requestRepository;

    public RequestServiceImpl(RequestRepository requestRepository) {
        this.requestRepository = requestRepository;
    }
    @Override
    public void create(String requestText, User user) {
        Request request = new Request(requestText, user);
        requestRepository.save(request);
    }

    @Override
    public List<Request> getAll() {
        return requestRepository.findAll(Sort.by(Sort.Direction.ASC, "creationTime"));
    }

    @Override
    public Request get(int requestId) {
        return requestRepository.findById(requestId).orElseThrow(() -> new RequestNotFoundException(requestId));
    }

    @Override
    public void update(Request request) {
        requestRepository.save(request);
    }
    @Override
    public void delete(int requestId) {
        requestRepository.deleteById(requestId);
    }
}
