package com.example.buckpal.account.domain;

import com.example.buckpal.account.domain.fixture.AccountBuilder;
import com.example.buckpal.account.domain.fixture.ActivityBuilder;
import org.example.buckpal.account.domain.Account;
import org.example.buckpal.account.domain.ActivityWindow;
import org.example.buckpal.account.domain.Money;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class AccountTest {

    private static Account createAccount() {
        Account.AccountId accountId = Account.AccountId.of(1L);

        return AccountBuilder.builder()
                .withId(accountId)
                .withBaselineBalance(Money.of(555L))
                .withActivityWindow(ActivityWindow.create(
                        ActivityBuilder.builder().withTargetAccountId(accountId).withMoney(999L).build(),
                        ActivityBuilder.builder().withTargetAccountId(accountId).withMoney(1L).build()))
                .build();
    }


    @DisplayName("AccountId should throw a NullPointerException")
    @Test
    void accountId_null_NullPointerException() {
        NullPointerException exception = assertThrows(NullPointerException.class, () -> Account.AccountId.of(null));
        assertEquals("value is marked non-null but is null", exception.getMessage());
    }

    @DisplayName("of should throw a NullPointerException in case of null balance or null activityWindow")
    @Test
    void of_nullValues_NullPointerException() {
        NullPointerException exception = assertThrows(NullPointerException.class, () -> Account.of(Money.ZERO, null));
        assertEquals("activityWindow is marked non-null but is null", exception.getMessage());

        exception = assertThrows(NullPointerException.class, () -> Account.of(null, ActivityWindow.create()));
        assertEquals("baselineBalance is marked non-null but is null", exception.getMessage());
    }

    @DisplayName("Should return the id as a optional")
    @Test
    void getId() {
        assertEquals(Optional.empty(), AccountBuilder.builder().withEmptyId().build().getId());

        Account.AccountId accountId = Account.AccountId.of(100L);
        assertEquals(Optional.of(accountId), AccountBuilder.builder().withId(accountId).build().getId());
    }

    @DisplayName("Should calculate the current balance")
    @Test
    void calculatesBalance() {
        assertEquals(Money.of(1555L), createAccount().calculateBalance());
    }

    @DisplayName("Withdrawal succeeds")
    @Test
    void withdrawal_succeeds() {
        Account account = createAccount();

        assertTrue(account.withdraw(Money.of(555L), Account.AccountId.of(99L)));
        assertEquals(3, account.getActivityWindow().getActivities().size());
        assertEquals(Money.of(1000L), account.calculateBalance());
    }

    @DisplayName("Withdrawal fail if you dont have enough money")
    @Test
    void withdrawal_failOnNotEnoughMoney() {
        Account account = createAccount();

        assertFalse(account.withdraw(Money.of(1556L), Account.AccountId.of(99L)));
        assertEquals(2, account.getActivityWindow().getActivities().size());
        assertEquals(Money.of(1555L), account.calculateBalance());
    }

    @DisplayName("Deposit success")
    @Test
    void deposit() {
        Account account = createAccount();

        assertTrue(account.deposit(Money.of(445L), Account.AccountId.of(99L)));
        assertEquals(3, account.getActivityWindow().getActivities().size());
        assertEquals(Money.of(2000L), account.calculateBalance());
    }
}
