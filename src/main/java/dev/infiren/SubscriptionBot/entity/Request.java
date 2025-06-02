package dev.infiren.SubscriptionBot.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
public class Request {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String text;
    @Setter
    private boolean isViewed;
    private LocalDateTime creationTime;
    @ManyToOne(targetEntity = User.class)
    @JoinColumn(name = "user_id")
    private User requester;

    public Request(String text, User requester) {
        this.text = text;
        this.requester = requester;
        this.isViewed = false;
        this.creationTime = LocalDateTime.now();
    }
}
