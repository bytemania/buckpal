package org.example.buckpal.account.application.service;

import lombok.RequiredArgsConstructor;
import org.example.buckpal.account.domain.Account;
import org.example.buckpal.account.port.in.SendMoneyCommand;
import org.example.buckpal.account.port.in.SendMoneyUseCase;
import org.example.buckpal.account.application.service.exception.ThresholdExceedException;
import org.example.buckpal.account.application.service.property.MoneyTransferProperties;
import org.example.buckpal.account.common.UseCase;
import org.example.buckpal.account.port.out.AccountLock;
import org.example.buckpal.account.port.out.LoadAccountPort;
import org.example.buckpal.account.port.out.UpdateAccountStatePort;

import javax.transaction.Transactional;
import java.time.LocalDateTime;

@RequiredArgsConstructor
@UseCase
@Transactional
public class SendMoneyService implements SendMoneyUseCase {

    private final LoadAccountPort loadAccountPort;
    private final AccountLock accountLock;
    private final UpdateAccountStatePort updateAccountStatePort;
    private final MoneyTransferProperties moneyTransferProperties;

    @Override
    public boolean sendMoney(SendMoneyCommand command) {

        checkThreshold(command);

        LocalDateTime baselineDate = LocalDateTime.now().minusDays(10);

        Account sourceAccount = loadAccountPort.loadAccount(command.getSourceAccountId(), baselineDate);
        Account targetAccount = loadAccountPort.loadAccount(command.getTargetAccountId(), baselineDate);

        Account.AccountId sourceAccountId = sourceAccount.getId()
                .orElseThrow(() -> new IllegalArgumentException("expected source account ID not to be empty"));

        Account.AccountId targetAccountId = targetAccount.getId()
                .orElseThrow(() -> new IllegalArgumentException("expected target account ID not to be empty"));

        accountLock.lockAccount(sourceAccountId);
        if (!sourceAccount.withdraw(command.getMoney(), targetAccountId)) {
            accountLock.releaseAccount(sourceAccountId);
            return false;
        }

        accountLock.lockAccount(targetAccountId);
        if (!targetAccount.deposit(command.getMoney(), sourceAccountId)) {
            accountLock.releaseAccount(sourceAccountId);
            accountLock.releaseAccount(targetAccountId);
            return false;
        }

        updateAccountStatePort.updateActivities(sourceAccount);
        updateAccountStatePort.updateActivities(targetAccount);

        accountLock.releaseAccount(sourceAccountId);
        accountLock.releaseAccount(targetAccountId);

        return true;
    }

    private void checkThreshold(SendMoneyCommand command) {
        if (command.getMoney().isGreaterThan(moneyTransferProperties.getMaximumTransferThreshold())) {
            throw new ThresholdExceedException(moneyTransferProperties.getMaximumTransferThreshold(), command.getMoney());
        }
    }

}
