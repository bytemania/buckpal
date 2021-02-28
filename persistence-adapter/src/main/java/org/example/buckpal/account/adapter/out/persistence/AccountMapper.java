package org.example.buckpal.account.adapter.out.persistence;

import org.example.buckpal.account.domain.Account;
import org.example.buckpal.account.domain.Activity;
import org.example.buckpal.account.domain.ActivityWindow;
import org.example.buckpal.account.domain.Money;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
class AccountMapper {

    Account mapToDomainEntity(
            AccountJpaEntity account,
            List<ActivityJpaEntity> activities,
            double withdrawalBalance,
            double depositBalance) {

        Money baselineBalance = Money.subtract(Money.of(depositBalance), Money.of(withdrawalBalance));

        return Account.of(Account.AccountId.of(account.getId()), baselineBalance, mapToActivityWindow(activities));
    }

    ActivityWindow mapToActivityWindow(List<ActivityJpaEntity> activities) {
        List<Activity> mappedActivities = activities.stream()
                .map(activityJpaEntity -> Activity.of(
                        Activity.ActivityId.of(activityJpaEntity.getId()),
                        Account.AccountId.of(activityJpaEntity.getOwnerAccountId()),
                        Account.AccountId.of(activityJpaEntity.getSourceAccountId()),
                        Account.AccountId.of(activityJpaEntity.getTargetAccountId()),
                        activityJpaEntity.getTimestamp(),
                        Money.of(activityJpaEntity.getAmount())
                        ))
                .collect(Collectors.toList());

        return new ActivityWindow(mappedActivities);
    }

    ActivityJpaEntity mapToJpaEntity(Activity activity) {
        return new ActivityJpaEntity(
                activity.getId().isPresent() ? activity.getId().get().getValue() : null,
                activity.getTimestamp(),
                activity.getOwnerAccountId().getValue(),
                activity.getSourceAccountId().getValue(),
                activity.getTargetAccountId().getValue(),
                activity.getMoney().getAmount().longValue());
    }

}
