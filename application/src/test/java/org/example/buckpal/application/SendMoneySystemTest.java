package org.example.buckpal.application;

import org.example.buckpal.account.domain.Account;
import org.example.buckpal.account.domain.Money;
import org.example.buckpal.account.port.out.LoadAccountPort;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;

import java.time.LocalDateTime;

import static org.assertj.core.api.BDDAssertions.then;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class SendMoneySystemTest {

    private static final Account.AccountId SOURCE_ACCOUNT_ID = Account.AccountId.of(1L);
    private static final Account.AccountId TARGET_ACCOUNT_ID = Account.AccountId.of(2L);
    private static final Money AMOUNT = Money.of(500);

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private LoadAccountPort loadAccountPort;

    @DisplayName("Should send money from one account to another one")
    @Test
    void sendMoney() {
        Money initialSourceBalance = sourceAccount().calculateBalance();
        Money initialTargetBalance = targetAccount().calculateBalance();

        ResponseEntity<?> response = whenSendMoney(SOURCE_ACCOUNT_ID, TARGET_ACCOUNT_ID, AMOUNT);

        then(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        then(sourceAccount().calculateBalance()).isEqualTo(initialSourceBalance.minus(AMOUNT));
        then(targetAccount().calculateBalance()).isEqualTo(initialTargetBalance.plus(AMOUNT));
    }

    private ResponseEntity<?> whenSendMoney(
            Account.AccountId sourceAccountId,
            Account.AccountId targetAccountId,
            Money amount) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        HttpEntity<Void> request = new HttpEntity<>(null, headers);

        return restTemplate.exchange(
                "/accounts/send/{sourceAccountId}/{targetAccountId}/{amount}",
                HttpMethod.POST,
                request,
                Object.class,
                sourceAccountId.getValue(),
                targetAccountId.getValue(),
                amount.getAmount());
    }

    private Account sourceAccount() {
        return loadAccount(SOURCE_ACCOUNT_ID);
    }

    private Account targetAccount() {
        return loadAccount(TARGET_ACCOUNT_ID);
    }

    private Account loadAccount(Account.AccountId accountId) {
        return loadAccountPort.loadAccount(accountId, LocalDateTime.now());
    }

    private Money balanceOf(Account.AccountId accountId) {
        return loadAccountPort.loadAccount(accountId, LocalDateTime.now()).calculateBalance();
    }
}
