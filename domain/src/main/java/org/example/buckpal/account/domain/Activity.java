package org.example.buckpal.account.domain;

import lombok.NonNull;
import lombok.Value;

import java.time.LocalDateTime;

@Value(staticConstructor = "of")
public class Activity {

    @NonNull
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

    @Value(staticConstructor = "of")
    public static class ActivityId {
        @NonNull
        Long value;
    }
}
