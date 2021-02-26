package org.example.buckpal.account.application.service;

import lombok.RequiredArgsConstructor;
import org.example.buckpal.account.application.port.in.SendMoneyCommand;
import org.example.buckpal.account.application.port.in.SendMoneyUseCase;
import org.example.buckpal.account.application.service.exception.ThresholdExceedException;
import org.example.buckpal.account.application.service.property.MoneyTransferProperties;
import org.example.buckpal.account.common.UseCase;

import javax.transaction.Transactional;
import java.time.LocalDateTime;

@RequiredArgsConstructor
@UseCase
@Transactional
public class SendMoneyService implements SendMoneyUseCase {
    //private final LoadAccountPort loadAccountPort;
    //private final AccountLock accountLock;
    //private final UpdateAccountStatePort updateAccountStatePort;
    private final MoneyTransferProperties moneyTransferProperties;

    @Override
    public boolean sendMoney(SendMoneyCommand command) {

        LocalDateTime baselineDate = LocalDateTime.now().minusDays(10);

        //Account sourceAccount = ;
        //Account targetAccount = ;



        return true;
    }

    private void checkThreshold(SendMoneyCommand command) {
        if (command.getMoney().isGreaterThan(moneyTransferProperties.getMaximumTransferThreshold())) {
            throw new ThresholdExceedException(moneyTransferProperties.getMaximumTransferThreshold(), command.getMoney());
        }
    }

}
