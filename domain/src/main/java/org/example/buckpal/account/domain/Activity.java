package org.example.buckpal.account.domain;

import lombok.*;

import java.time.LocalDateTime;
import java.util.Optional;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class Activity {

    public static Activity of(Account.AccountId ownerAccountId,
                              Account.AccountId sourceAccountId,
                              Account.AccountId targetAccountId,
                              LocalDateTime timestamp,
                              Money money) {
        return new Activity(null, ownerAccountId, sourceAccountId, targetAccountId, timestamp, money);
    }

    public static Activity of(ActivityId id,
                     Account.AccountId ownerAccountId,
                     Account.AccountId sourceAccountId,
                     Account.AccountId targetAccountId,
                     LocalDateTime timestamp,
                     Money money) {
        return new Activity(id, ownerAccountId, sourceAccountId, targetAccountId, timestamp, money);
    }

    @Value(staticConstructor = "of")
    public static class ActivityId {
        @NonNull
        Long value;
    }

    ActivityId id;

    @NonNull
    Account.AccountId ownerAccountId;

    @NonNull
    Account.AccountId sourceAccountId;

    @NonNull
    Account.AccountId targetAccountId;

    @NonNull
    LocalDateTime timestamp;

    @NonNull
    Money money;

    public Optional<ActivityId> getId() {
        return Optional.ofNullable(this.id);
    }
}
