package com.example.buckpal.account.domain;

import com.example.buckpal.account.domain.fixture.ActivityBuilder;
import org.example.buckpal.account.domain.Account;
import org.example.buckpal.account.domain.Activity;
import org.example.buckpal.account.domain.Money;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;


class ActivityTest {

    @DisplayName("ActivityId Should throw an NullPointerException in case of a nullValue")
    @Test
    void activityIdOf_nullValues_NullPointerException() {
        NullPointerException exception = assertThrows(NullPointerException.class, () -> Activity.ActivityId.of(null));
        assertEquals("value is marked non-null but is null", exception.getMessage());
    }

    @DisplayName("Should throw an NullPointerException in case of a nullValue")
    @Test
    void of_nullValues_NullPointerException() {
        NullPointerException exception;

        exception = assertThrows(NullPointerException.class, () -> Activity.of(
                Activity.ActivityId.of(1L),
                Account.AccountId.of(1L),
                Account.AccountId.of(2L),
                Account.AccountId.of(3L),
                LocalDateTime.now(),
                null));
        assertEquals("money is marked non-null but is null", exception.getMessage());

        exception = assertThrows(NullPointerException.class, () -> Activity.of(
                Activity.ActivityId.of(1L),
                Account.AccountId.of(1L),
                Account.AccountId.of(2L),
                Account.AccountId.of(3L),
                null,
                Money.of(100)));
        assertEquals("timestamp is marked non-null but is null", exception.getMessage());

        exception = assertThrows(NullPointerException.class, () -> Activity.of(
                Activity.ActivityId.of(1L),
                Account.AccountId.of(1L),
                Account.AccountId.of(2L),
                null,
                LocalDateTime.now(),
                Money.of(100)));
        assertEquals("targetAccountId is marked non-null but is null", exception.getMessage());

        exception = assertThrows(NullPointerException.class, () -> Activity.of(
                Activity.ActivityId.of(1L),
                Account.AccountId.of(1L),
                null,
                Account.AccountId.of(3L),
                LocalDateTime.now(),
                Money.of(100)));
        assertEquals("sourceAccountId is marked non-null but is null", exception.getMessage());

        exception = assertThrows(NullPointerException.class, () -> Activity.of(
                Activity.ActivityId.of(1L),
                null,
                Account.AccountId.of(2L),
                Account.AccountId.of(3L),
                LocalDateTime.now(),
                Money.of(100)));
        assertEquals("ownerAccountId is marked non-null but is null", exception.getMessage());
    }

    @DisplayName("Should get the activityId")
    @Test
    void getId() {
        assertEquals(Optional.empty(), ActivityBuilder.builder().withEmptyId().build().getId());
        Activity.ActivityId activityId = Activity.ActivityId.of(100L);
        assertEquals(Optional.of(activityId), ActivityBuilder.builder().withId(activityId).build().getId());
    }

}
