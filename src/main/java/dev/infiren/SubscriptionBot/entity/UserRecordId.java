package dev.infiren.SubscriptionBot.entity;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class UserRecordId implements Serializable {
    private User user;
    private Record record;
}
