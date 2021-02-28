package org.example.buckpal.account.adapter.in.web;


import lombok.RequiredArgsConstructor;
import org.example.buckpal.account.common.WebAdapter;
import org.example.buckpal.account.domain.Account;
import org.example.buckpal.account.domain.Money;
import org.example.buckpal.account.port.in.SendMoneyCommand;
import org.example.buckpal.account.port.in.SendMoneyUseCase;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@WebAdapter
@RestController
@RequiredArgsConstructor
public class SendMoneyController {

    private final SendMoneyUseCase sendMoneyUseCase;

    @PostMapping(path = "/accounts/send/{sourceAccountId}/{targetAccountId}/{amount}")
    public void sendMoney(@PathVariable("sourceAccountId") Long sourceAccountId,
                   @PathVariable("targetAccountId") Long targetAccountId,
                   @PathVariable("amount") Double amount) {

        SendMoneyCommand command = new SendMoneyCommand(
                Account.AccountId.of(sourceAccountId),
                Account.AccountId.of(targetAccountId),
                Money.of(amount));

        sendMoneyUseCase.sendMoney(command);
    }

}
