package dev.infiren.SubscriptionBot.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@NoArgsConstructor
@Getter
@Setter
@ToString
public class Record {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private LocalDateTime dateOfPayment;
    @Column(unique = true)
    private String code;
    @ManyToOne
    private User creator;
    @ManyToOne(fetch = FetchType.EAGER)
    private Subscription subscriptionType;
    @OneToMany(mappedBy = "record", orphanRemoval = true)
    private Set<UserRecord> userRecords = new HashSet<>();
}
