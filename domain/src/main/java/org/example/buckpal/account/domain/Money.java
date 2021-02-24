package org.example.buckpal.account.domain;

import lombok.NonNull;
import lombok.Value;

import java.math.BigDecimal;

@Value(staticConstructor = "of")
public class Money {

    public static final Money ZERO = Money.of(BigDecimal.ZERO);

    public static Money of(long value) {
        return new Money(BigDecimal.valueOf(value));
    }

    public static Money of(float value) {
        return new Money(BigDecimal.valueOf(value));
    }

    public static Money of(double value) {
        return new Money(BigDecimal.valueOf(value));
    }

    public static Money add(Money a, Money b) {
        return Money.of(a.amount.add(b.amount));
    }

    public static Money subtract(Money a, Money b) {
        return Money.of(a.amount.subtract(b.amount));
    }

    @NonNull
    BigDecimal amount;

    public boolean isPositiveOrZero() {
        return amount.compareTo(BigDecimal.ZERO) >= 0;
    }

    public boolean isPositive() {
        return amount.compareTo(BigDecimal.ZERO) > 0;
    }

    public boolean isNegative() {
        return amount.compareTo(BigDecimal.ZERO) < 0;
    }

    public boolean isGreaterThanOrEqualTo(Money money) {
        return amount.compareTo(money.amount) >= 0;
    }

    public boolean isGreaterThan(Money money) {
        return amount.compareTo(money.amount) >= 1;
    }

    public Money plus(Money money) {
        return Money.of(amount.add(money.amount));
    }

    public Money minus(Money money) {
        return Money.of(amount.subtract(money.amount));
    }

    public Money negate() {
        return Money.of(amount.negate());
    }

}
