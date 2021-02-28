package org.example.buckpal.account.port.out;

import org.example.buckpal.account.domain.Account;

public interface AccountLock {

    void lockAccount(Account.AccountId accountId);

    void releaseAccount(Account.AccountId accountId);
}
