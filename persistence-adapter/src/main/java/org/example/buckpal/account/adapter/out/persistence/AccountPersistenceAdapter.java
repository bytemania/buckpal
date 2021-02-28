package org.example.buckpal.account.adapter.out.persistence;

import lombok.RequiredArgsConstructor;
import org.example.buckpal.account.common.PersistenceAdapter;
import org.example.buckpal.account.domain.Account;
import org.example.buckpal.account.port.out.AccountLock;
import org.example.buckpal.account.port.out.LoadAccountPort;
import org.example.buckpal.account.port.out.UpdateAccountStatePort;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@PersistenceAdapter
public class AccountPersistenceAdapter implements LoadAccountPort, UpdateAccountStatePort, AccountLock {

    private final AccountRepository accountRepository;
    private final ActivityRepository activityRepository;
    private final AccountMapper accountMapper;

    @Override
    public Account loadAccount(Account.AccountId accountId, LocalDateTime baselineDate) {
        AccountJpaEntity account = accountRepository.findById(accountId.getValue()).orElseThrow(EntityNotFoundException::new);
        List<ActivityJpaEntity> activities = activityRepository.findByOwnerSince(accountId.getValue(), baselineDate);
        Long withdrawalBalance = orZero(activityRepository.getWithdrawalBalanceUntil(accountId.getValue(), baselineDate));
        Long depositBalance = orZero(activityRepository.getDepositBalanceUntil(accountId.getValue(), baselineDate));

        return accountMapper.mapToDomainEntity(account, activities, withdrawalBalance, depositBalance);
    }

    @Override
    public void updateActivities(Account account) {
        account.getActivityWindow().getActivities().stream()
                .filter(activity -> activity.getId().isEmpty())
                .map(accountMapper::mapToJpaEntity)
                .forEach(activityRepository::save);
    }

    private Long orZero(Long value) {
        return value == null ? 0L : value;
    }

    @Override
    public void lockAccount(Account.AccountId accountId) {
        //do nothing
    }

    @Override
    public void releaseAccount(Account.AccountId accountId) {
        //do nothing
    }
}
