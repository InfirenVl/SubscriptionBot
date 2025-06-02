package dev.infiren.SubscriptionBot.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;

@Entity
@Getter
@ToString
@NoArgsConstructor
@EqualsAndHashCode()
public class Subscription {
    @Id
    @EqualsAndHashCode.Exclude
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;
    private int price;
    private int maxMembers;

    public Subscription(String name, int price, int maxMembers) {
        this.name = name;
        this.price = price;
        this.maxMembers = maxMembers;
    }
}
