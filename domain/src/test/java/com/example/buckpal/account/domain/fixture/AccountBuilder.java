package com.example.buckpal.account.domain.fixture;

import lombok.NonNull;
import org.example.buckpal.account.domain.Account;
import org.example.buckpal.account.domain.ActivityWindow;
import org.example.buckpal.account.domain.Money;

public class AccountBuilder {

    public static AccountBuilder builder() {
        return new AccountBuilder();
    }

    private Account.AccountId id = Account.AccountId.of(Util.rand());
    private Money baselineBalance = Money.of(Util.rand());
    private ActivityWindow activityWindow = ActivityWindow.create(ActivityBuilder.builder().build());

    public AccountBuilder withEmptyId() {
        this.id = null;
        return this;
    }

    public AccountBuilder withId(long accountId) {
        this.id = Account.AccountId.of(accountId);
        return this;
    }

    public AccountBuilder withId(@NonNull Account.AccountId accountId) {
        this.id = accountId;
        return this;
    }

    public AccountBuilder withBaselineBalance(@NonNull Money baselineBalance) {
        this.baselineBalance = baselineBalance;
        return this;
    }

    public AccountBuilder withActivityWindow(@NonNull ActivityWindow activityWindow) {
        this.activityWindow = activityWindow;
        return this;
    }

    public Account build() {
        return Account.of(id, baselineBalance, activityWindow);
    }
}
