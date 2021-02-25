package org.example.buckpal.account.domain;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class ActivityWindow {

    public static ActivityWindow create(Activity... activities) {
        return new ActivityWindow(Arrays.stream(activities).collect(Collectors.toList()));
    }

    @NonNull
    private final List<Activity> activities;

    public List<Activity> getActivities() {
        return Collections.unmodifiableList(activities);
    }

    public void addActivity(Activity activity) {
        activities.add(activity);
    }

    public LocalDateTime getStartTimestamp() {
        return activities.stream()
                .min(Comparator.comparing(Activity::getTimestamp))
                .orElseThrow(IllegalArgumentException::new)
                .getTimestamp();
    }

    public LocalDateTime getEndTimestamp() {
        return activities.stream()
                .max(Comparator.comparing(Activity::getTimestamp))
                .orElseThrow(IllegalArgumentException::new)
                .getTimestamp();
    }

    public Money calculateBalance(Account.AccountId accountId) {
        Money depositBalance = activities.stream()
                .filter(a -> a.getTargetAccountId().equals(accountId))
                .map(Activity::getMoney)
                .reduce(Money.ZERO, Money::add);

        Money withdrawalBalance = activities.stream()
                .filter(a -> a.getSourceAccountId().equals(accountId))
                .map(Activity::getMoney)
                .reduce(Money.ZERO, Money::add);

        return Money.add(depositBalance, withdrawalBalance.negate());
    }
}
