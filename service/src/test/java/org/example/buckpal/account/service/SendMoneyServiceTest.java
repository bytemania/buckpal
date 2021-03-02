package org.example.buckpal.account.service;

import org.example.buckpal.account.service.exception.ThresholdExceedException;
import org.example.buckpal.account.service.property.MoneyTransferProperties;
import org.example.buckpal.account.domain.Account;
import org.example.buckpal.account.domain.Money;
import org.example.buckpal.account.port.in.SendMoneyCommand;
import org.example.buckpal.account.port.out.AccountLock;
import org.example.buckpal.account.port.out.LoadAccountPort;
import org.example.buckpal.account.port.out.UpdateAccountStatePort;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;

class SendMoneyServiceTest {

   private final LoadAccountPort loadAccountPort = Mockito.mock(LoadAccountPort.class);
   private final AccountLock accountLock = Mockito.mock(AccountLock.class);
   private final UpdateAccountStatePort updateAccountStatePort = Mockito.mock(UpdateAccountStatePort.class);
   private final SendMoneyService sendMoneyService = new SendMoneyService(loadAccountPort, accountLock, updateAccountStatePort,
           new MoneyTransferProperties(Money.of(1000)));

   @DisplayName("Should fail if the amount to transfer exceed the limit")
   @Test
   void transferExceedLimit() {
      SendMoneyCommand command = SendMoneyCommand.of(Account.AccountId.of(1L), Account.AccountId.of(2L), Money.of(10_000L));
      ThresholdExceedException exception = assertThrows(ThresholdExceedException.class, () -> sendMoneyService.sendMoney(command));
      assertThat(exception.getMessage()).isEqualTo("Maximum threshold for transferring money exceeded: tried to transfer Money(amount=10000) but threshold is Money(amount=1000)!");
   }

   @DisplayName("Given an inexistent source account it should return an EmptyAccount Exception")
   @Test
   void givenAnNonExistSourceAccount() {
      Account.AccountId sourceAccountId = Account.AccountId.of(41L);
      given(loadAccountPort.loadAccount(eq(sourceAccountId), any(LocalDateTime.class))).willThrow(EntityNotFoundException.class);
      SendMoneyCommand command = SendMoneyCommand.of(sourceAccountId, Account.AccountId.of(42L), Money.of(300L));

      assertThrows(EntityNotFoundException.class, () -> sendMoneyService.sendMoney(command));
   }

   @DisplayName("Given an inexistent target account it should return an EmptyAccount Exception")
   @Test
   void givenAnNonExistTargetAccount() {
      Account.AccountId targetAccountId = Account.AccountId.of(42L);
      SendMoneyCommand command = SendMoneyCommand.of(Account.AccountId.of(41L), targetAccountId, Money.of(300L));
      given(loadAccountPort.loadAccount(eq(targetAccountId), any(LocalDateTime.class))).willThrow(EntityNotFoundException.class);

      assertThrows(EntityNotFoundException.class, () -> sendMoneyService.sendMoney(command));
   }

   @DisplayName("Given a Withdrawal Fail implies a temporary lock and release of source account only")
   @Test
   void givenWithdrawalFails_thenOnlySourceAccountIsLockedAndReleased() {
      Account sourceAccount = givenSourceAccount();
      Account targetAccount = givenTargetAccount();

      givenWithdrawalWillFail(sourceAccount);

      SendMoneyCommand command = SendMoneyCommand.of(sourceAccount.getId().get(), targetAccount.getId().get(), Money.of(300L));

      assertThat(sendMoneyService.sendMoney(command)).isFalse();
      then(accountLock).should().lockAccount(eq(sourceAccount.getId().get()));
      then(accountLock).should(times(0)).lockAccount(eq(targetAccount.getId().get()));
   }

   @DisplayName("Given Deposit Fails")
   @Test
   void depositFails() {
      Account sourceAccount = givenSourceAccount();
      Account targetAccount = givenTargetAccount();

      givenWithdrawalWillSucceed(sourceAccount);
      givenDepositWillFail(targetAccount);

      Money money = Money.of(500L);

      Account.AccountId sourceAccountId = sourceAccount.getId().get();
      Account.AccountId targetAccountId = targetAccount.getId().get();

      SendMoneyCommand command = SendMoneyCommand.of(sourceAccountId, targetAccountId, money);

      assertThat(sendMoneyService.sendMoney(command)).isFalse();

      then(accountLock).should().lockAccount(eq(sourceAccountId));
      then(sourceAccount).should().withdraw(eq(money), eq(targetAccountId));
      then(accountLock).should().releaseAccount(eq(sourceAccountId));

      then(accountLock).should().lockAccount(eq(targetAccountId));
      then(targetAccount).should().deposit(eq(money), eq(sourceAccountId));
      then(accountLock).should().releaseAccount(eq(targetAccountId));
   }

   @DisplayName("Given a source and a target account if they are valid a transfer should succeed")
   @Test
   void transactionSucceeds() {
      Account sourceAccount = givenSourceAccount();
      Account targetAccount = givenTargetAccount();

      givenWithdrawalWillSucceed(sourceAccount);
      givenDepositWillSucceed(targetAccount);

      Account.AccountId sourceAccountId = sourceAccount.getId().get();
      Account.AccountId targetAccountId = targetAccount.getId().get();

      Money money = Money.of(500L);

      SendMoneyCommand command = SendMoneyCommand.of(sourceAccountId, targetAccountId, money);

      assertThat(sendMoneyService.sendMoney(command)).isTrue();

      then(accountLock).should().lockAccount(eq(sourceAccountId));
      then(sourceAccount).should().withdraw(eq(money), eq(targetAccountId));
      then(accountLock).should().releaseAccount(eq(sourceAccountId));

      then(accountLock).should().lockAccount(eq(targetAccountId));
      then(targetAccount).should().deposit(eq(money), eq(sourceAccountId));
      then(accountLock).should().releaseAccount(eq(targetAccountId));

      thenAccountsHaveBeenUpdated(sourceAccountId, targetAccountId);
   }

   private void thenAccountsHaveBeenUpdated(Account.AccountId... accountIds) {
      ArgumentCaptor<Account> accountCaptor = ArgumentCaptor.forClass(Account.class);
      then(updateAccountStatePort).should(times(accountIds.length)).updateActivities(accountCaptor.capture());

      List<Account.AccountId> updateAccountIds = accountCaptor.getAllValues().stream()
              .map(Account::getId)
              .map(Optional::get)
              .collect(Collectors.toList());

      for (Account.AccountId accountId: accountIds) {
         assertThat(updateAccountIds).contains(accountId);
      }
   }

   private void givenDepositWillSucceed(Account account) {
      given(account.deposit(any(Money.class), any(Account.AccountId.class))).willReturn(true);
   }

   private void givenDepositWillFail(Account account) {
      given(account.deposit(any(Money.class), any(Account.AccountId.class))).willReturn(false);
   }

   private void givenWithdrawalWillSucceed(Account account) {
      given(account.withdraw(any(Money.class), any(Account.AccountId.class))).willReturn(true);
   }

   private void givenWithdrawalWillFail(Account account) {
      given(account.withdraw(any(Money.class), any(Account.AccountId.class))).willReturn(false);
   }

   private Account givenSourceAccount() {
      return givenAnAccountWithId(Account.AccountId.of(41L));
   }

   private Account givenTargetAccount() {
      return givenAnAccountWithId(Account.AccountId.of(42L));
   }

   private Account givenAnAccountWithId(Account.AccountId id) {
      Account account = Mockito.mock(Account.class);
      given(account.getId()).willReturn(Optional.of(id));
      given(loadAccountPort.loadAccount(eq(account.getId().get()), any(LocalDateTime.class))).willReturn(account);
      return account;
   }
}