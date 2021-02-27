package org.example.buckpal.account.port.in;

import org.example.buckpal.account.domain.Account;
import org.example.buckpal.account.domain.Money;

public interface GetAccountBalanceQuery {

    Money getAccountBalance(Account.AccountId accountId);

}
