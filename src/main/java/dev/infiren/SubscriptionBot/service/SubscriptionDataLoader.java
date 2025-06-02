package dev.infiren.SubscriptionBot.service;

import com.fasterxml.jackson.core.exc.StreamReadException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.infiren.SubscriptionBot.entity.Subscription;
import dev.infiren.SubscriptionBot.repository.SubscriptionRepository;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class SubscriptionDataLoader implements ApplicationRunner {
    private final SubscriptionRepository subscriptionRepository;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public SubscriptionDataLoader(SubscriptionRepository subscriptionRepository) {
        this.subscriptionRepository = subscriptionRepository;
    }

    @Override
    public void run(ApplicationArguments args) {
//        try {
//            List<Subscription> subscriptions = objectMapper.readValue(TypeReference.class.getResourceAsStream("/json/subscriptions.json"), new TypeReference<List<Subscription>>(){});
//            subscriptionRepository.saveAll(subscriptions);
//        } catch (StreamReadException e) {
//            throw new RuntimeException(e);
//        } catch (DatabindException e) {
//            throw new RuntimeException(e);
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
    }
}
