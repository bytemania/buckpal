package com.example.buckpal.account.domain.fixture;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.example.buckpal.account.domain.Account;
import org.example.buckpal.account.domain.Activity;
import org.example.buckpal.account.domain.Money;

import java.time.LocalDateTime;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ActivityBuilder {

    public static ActivityBuilder builder() {
        return new ActivityBuilder();
    }

    private Activity.ActivityId id = Activity.ActivityId.of(Util.rand());
    private final Account.AccountId ownerAccountId = Account.AccountId.of(Util.rand());
    private Account.AccountId sourceAccountId = Account.AccountId.of(Util.rand());
    private Account.AccountId targetAccountId = Account.AccountId.of(Util.rand());
    private LocalDateTime timestamp = LocalDateTime.now();
    private Money money = Money.of(Util.rand());

    public ActivityBuilder withEmptyId() {
        this.id = null;
        return this;
    }

    public ActivityBuilder withId(Activity.ActivityId activityId) {
        this.id = activityId;
        return this;
    }

    public ActivityBuilder withSourceAccountId(long sourceAccountId) {
        this.sourceAccountId = Account.AccountId.of(sourceAccountId);
        return this;
    }

    public ActivityBuilder withSourceAccountId(Account.AccountId sourceAccountId) {
        this.sourceAccountId = sourceAccountId;
        return this;
    }

    public ActivityBuilder withTargetAccountId(long targetAccountId) {
        this.targetAccountId = Account.AccountId.of(targetAccountId);
        return this;
    }

    public ActivityBuilder withTargetAccountId(Account.AccountId targetAccountId) {
        this.targetAccountId = targetAccountId;
        return this;
    }

    public ActivityBuilder withTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
        return this;
    }

    public ActivityBuilder withMoney(long money) {
        this.money = Money.of(money);
        return this;
    }

    public Activity build() {
        return Activity.of(id, ownerAccountId, sourceAccountId, targetAccountId, timestamp, money);
    }
}