package org.example.buckpal.account.port.out;

import org.example.buckpal.account.domain.Account;

import java.time.LocalDateTime;

public interface LoadAccountPort {
    Account loadAccount(Account.AccountId accountId, LocalDateTime baselineDate);
}
