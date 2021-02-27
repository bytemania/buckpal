package org.example.buckpal.account.application.service.exception;

import org.example.buckpal.account.domain.Money;

public class ThresholdExceedException extends RuntimeException {

    public ThresholdExceedException(Money threshold, Money actual) {
        super(String.format("Maximum threshold for transferring money exceeded: tried to transfer %s but threshold is %s!", actual, threshold));
    }
}
