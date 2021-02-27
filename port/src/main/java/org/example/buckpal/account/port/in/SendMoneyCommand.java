package org.example.buckpal.account.port.in;


import lombok.EqualsAndHashCode;
import lombok.Value;
import org.example.buckpal.account.common.SelfValidating;
import org.example.buckpal.account.domain.Account;
import org.example.buckpal.account.domain.Money;

import javax.validation.constraints.NotNull;

@Value
@EqualsAndHashCode(callSuper = false)
public class SendMoneyCommand extends SelfValidating<SendMoneyCommand> {

    @NotNull
    Account.AccountId sourceAccountId;

    @NotNull
    Account.AccountId targetAccountId;

    @NotNull
    Money money;

    public SendMoneyCommand(
            Account.AccountId sourceAccountId,
            Account.AccountId targetAccountId,
            Money money) {
        this.sourceAccountId = sourceAccountId;
        this.targetAccountId = targetAccountId;
        this.money = money;
        this.validateSelf();
    }
}
