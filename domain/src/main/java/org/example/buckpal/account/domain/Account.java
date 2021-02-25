package org.example.buckpal.account.domain;

import lombok.*;

import java.time.LocalDateTime;
import java.util.Optional;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Account {

    @Value(staticConstructor = "of")
    public static class AccountId {
        @NonNull
        Long value;
    }

    public static Account of(Money baselineBalance,
                         ActivityWindow activityWindow) {
        return of(null, baselineBalance, activityWindow);
    }

    public static Account of(AccountId accountId,
                             Money baselineBalance,
                             ActivityWindow activityWindow) {
        return new Account(accountId, baselineBalance, activityWindow);
    }

    private final AccountId id;

    @NonNull
    @Getter
    private final Money baselineBalance;

    @NonNull
    @Getter
    private final ActivityWindow activityWindow;

    public Optional<AccountId> getId() {
        return Optional.ofNullable(this.id);
    }

    public Money calculateBalance() {
        return Money.add(this.baselineBalance, this.activityWindow.calculateBalance(this.id));
    }

    public boolean withdraw(Money money, AccountId targetAccountId) {
        boolean mayWithdraw = Money.add(calculateBalance(), money.negate()).isPositiveOrZero();

        if (!mayWithdraw) {
            return  false;
        } else {
            Activity withdrawal = Activity.of(this.id, this.id, targetAccountId, LocalDateTime.now(), money);
            this.activityWindow.addActivity(withdrawal);
            return true;
        }
    }

    public boolean deposit(Money money, AccountId sourceAccountId) {
        Activity deposit = Activity.of(this.id, sourceAccountId, this.id, LocalDateTime.now(), money);
        this.activityWindow.addActivity(deposit);
        return true;
    }
}
