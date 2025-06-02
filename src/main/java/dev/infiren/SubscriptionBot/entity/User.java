package dev.infiren.SubscriptionBot.entity;

import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import lombok.*;
import org.apache.commons.lang3.builder.EqualsExclude;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "users")
@Getter
@ToString
@NoArgsConstructor
@EqualsAndHashCode
public class User {
    @Id
    @EqualsAndHashCode.Exclude
    private long id;
    private String name;

    @Setter
    private int menuMessageId = -1;

    @Setter
    @Enumerated(EnumType.STRING)
    private Statement statement = Statement.NONE;
    private String metadata = "";

    @OneToMany(orphanRemoval = true, mappedBy = "requester")
    @EqualsAndHashCode.Exclude
    private Set<Request> requests;

    @OneToMany(mappedBy = "user", orphanRemoval = true)
    @EqualsAndHashCode.Exclude
    private List<UserRecord> userRecords = new ArrayList<>();

    public User(long id, String name) {
        this.id = id;
        this.name = name;
    }

    public void addMetadata(String data) {
        metadata += data +"`@`";
    }
    public void resetMetadata() {
        metadata = "";
    }
    public List<String> getMetadata() {
        return List.of(metadata.split("`@`"));
    }

    public enum Statement {
        NONE,
        ENTERING_CODE,
        ENTERING_REQUEST,
        CREATING_SUBSCRIPTION,
    }
}
