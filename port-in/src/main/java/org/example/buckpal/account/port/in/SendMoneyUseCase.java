package org.example.buckpal.account.port.in;

public interface SendMoneyUseCase {
    boolean sendMoney(SendMoneyCommand command);
}
