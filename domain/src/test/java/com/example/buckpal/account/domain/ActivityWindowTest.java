package com.example.buckpal.account.domain;

import com.example.buckpal.account.domain.fixture.ActivityBuilder;
import org.example.buckpal.account.domain.Account;
import org.example.buckpal.account.domain.Activity;
import org.example.buckpal.account.domain.ActivityWindow;
import org.example.buckpal.account.domain.Money;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ActivityWindowTest {

    private static final LocalDateTime START_DATE = LocalDateTime.of(2021, 2, 1, 0 , 0);
    private static final LocalDateTime BETWEEN_DATE = LocalDateTime.of(2021, 2, 15, 0 , 0);
    private static final LocalDateTime END_DATE = LocalDateTime.of(2021, 2, 28, 0 , 0);

    @DisplayName("Should create a WindowActivity with the static constructor")
    @Test
    void create() {
        assertEquals(Collections.EMPTY_LIST, ActivityWindow.create().getActivities());

        Activity activity0 = ActivityBuilder.builder().build();
        Activity activity1 = ActivityBuilder.builder().build();

        assertEquals(List.of(activity0), ActivityWindow.create(activity0).getActivities());
        assertEquals(List.of(activity0, activity1), ActivityWindow.create(activity0, activity1).getActivities());
    }

    @DisplayName("Should return a NullPointerException in case of null argument")
    @Test
    void activityWindow_null_nullPointerException() {
        NullPointerException exception = assertThrows(NullPointerException.class, () -> new ActivityWindow(null));
        assertEquals("activities is marked non-null but is null", exception.getMessage());
    }

    @DisplayName("Should create add a new Activity to the WindowActivity")
    @Test
    void add() {
        ActivityWindow activityWindow = ActivityWindow.create();
        Activity activity0 = ActivityBuilder.builder().build();
        Activity activity1 = ActivityBuilder.builder().build();

        assertEquals(Collections.EMPTY_LIST, activityWindow.getActivities());

        activityWindow.addActivity(activity0);
        assertEquals(List.of(activity0), activityWindow.getActivities());

        activityWindow.addActivity(activity1);
        assertEquals(List.of(activity0, activity1), activityWindow.getActivities());
    }

    @DisplayName("Should return the startTimestamp from a list of activity")
    @Test
    void getStartTimestamp() {
        ActivityWindow activityWindow = ActivityWindow.create();

        assertThrows(IllegalArgumentException.class, activityWindow::getStartTimestamp);

        activityWindow.addActivity(ActivityBuilder.builder().withTimestamp(START_DATE).build());
        activityWindow.addActivity(ActivityBuilder.builder().withTimestamp(BETWEEN_DATE).build());
        activityWindow.addActivity(ActivityBuilder.builder().withTimestamp(END_DATE).build());

        assertEquals(START_DATE, activityWindow.getStartTimestamp());
    }

    @DisplayName("Should return the endTimestamp from a list of activity")
    @Test
    void getEndTimestamp() {
        ActivityWindow activityWindow = ActivityWindow.create();

        assertThrows(IllegalArgumentException.class, activityWindow::getEndTimestamp);

        activityWindow.addActivity(ActivityBuilder.builder().withTimestamp(START_DATE).build());
        activityWindow.addActivity(ActivityBuilder.builder().withTimestamp(BETWEEN_DATE).build());
        activityWindow.addActivity(ActivityBuilder.builder().withTimestamp(END_DATE).build());

        assertEquals(END_DATE, activityWindow.getEndTimestamp());
    }

    @DisplayName("Should calculate the current balance")
    @Test
    void calculateBalance() {
        assertEquals(Money.ZERO, ActivityWindow.create().calculateBalance(Account.AccountId.of(1L)));

        final long ACCOUNT_ID_1 = 1L;
        final long ACCOUNT_ID_2 = 2L;

        ActivityWindow activityWindow = ActivityWindow.create(
                ActivityBuilder.builder()
                    .withSourceAccountId(ACCOUNT_ID_1)
                    .withTargetAccountId(ACCOUNT_ID_2)
                    .withMoney(999)
                    .build(),
                ActivityBuilder.builder()
                        .withSourceAccountId(ACCOUNT_ID_1)
                        .withTargetAccountId(ACCOUNT_ID_2)
                        .withMoney(1)
                        .build(),
                ActivityBuilder.builder()
                        .withSourceAccountId(ACCOUNT_ID_2)
                        .withTargetAccountId(ACCOUNT_ID_1)
                        .withMoney(500)
                        .build()
        );

        assertEquals(Money.of(-500), activityWindow.calculateBalance(Account.AccountId.of(ACCOUNT_ID_1)));
        assertEquals(Money.of(500), activityWindow.calculateBalance(Account.AccountId.of(ACCOUNT_ID_2)));
    }
}
