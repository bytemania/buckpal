package org.example.buckpal.account.port.out;

import org.example.buckpal.account.domain.Account;

public interface UpdateAccountStatePort {

    void updateActivities(Account account);

}
