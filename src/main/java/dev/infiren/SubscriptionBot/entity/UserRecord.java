package dev.infiren.SubscriptionBot.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.ManyToOne;
import lombok.*;

@Entity
@Setter
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@IdClass(UserRecordId.class)
public class UserRecord {
    @Id
    @ManyToOne
    private User user;
    @Id
    @ManyToOne()
    private Record record;
    private boolean isActive;

}
