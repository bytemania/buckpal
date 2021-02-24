package com.example.buckpal.account.domain;

import org.example.buckpal.account.domain.Money;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class MoneyTest {

    @DisplayName("Should throw a NullPointerException in case of Error")
    @Test
    void of_nullAmount_NullPointerException() {
       Throwable exception = assertThrows(NullPointerException.class, () -> Money.of(null));
       assertEquals("amount is marked non-null but is null", exception.getMessage());
    }

    @DisplayName("Should create a money from a Long amount")
    @Test
    void of_longAmount() {
        assertEquals(BigDecimal.valueOf(100L), Money.of(100L).getAmount());
    }

    @DisplayName("Should create a money from a Float amount")
    @Test
    void of_floatAmount() {
        assertEquals(BigDecimal.valueOf(100.0F), Money.of(100.0F).getAmount());
    }

    @DisplayName("Should create a money from a Double amount")
    @Test
    void of_doubleAmount() {
        assertEquals(BigDecimal.valueOf(100.0D), Money.of(100.0D).getAmount());
    }

    @DisplayName("Should sum two moneys")
    @Test
    void add_twoMoney() {
        assertEquals(Money.of(300), Money.add(Money.of(100), Money.of(200)));
    }

    @DisplayName("Should subtract two moneys")
    @Test
    void subtract_twoMoney() {
        assertEquals(Money.of(50), Money.subtract(Money.of(100), Money.of(50)));
    }

    @DisplayName("Should verify if is Positive Or Zero")
    @Test
    void isPositiveZero() {
        assertTrue(Money.of(0).isPositiveOrZero());
        assertTrue(Money.ZERO.isPositiveOrZero());
        assertTrue(Money.of(100).isPositiveOrZero());
        assertFalse(Money.of(-100).isPositiveOrZero());
    }

    @DisplayName("Should verify if is Positive")
    @Test
    void isPositive() {
        assertFalse(Money.of(0).isPositive());
        assertFalse(Money.ZERO.isPositive());
        assertTrue(Money.of(100).isPositive());
        assertFalse(Money.of(-100).isPositive());
    }

    @DisplayName("Should verify if is Negative")
    @Test
    void isNegative() {
        assertFalse(Money.of(0).isNegative());
        assertFalse(Money.ZERO.isNegative());
        assertFalse(Money.of(100).isNegative());
        assertTrue(Money.of(-100).isNegative());
    }

    @DisplayName("Should verify isGreaterThanOrEqualTo")
    @Test
    void isGreaterThanEqualTo() {
        assertTrue(Money.ZERO.isGreaterThanOrEqualTo(Money.ZERO));
        assertTrue(Money.of(300).isGreaterThanOrEqualTo(Money.of(200)));
        assertFalse(Money.of(100).isGreaterThanOrEqualTo(Money.of(200)));
    }

    @DisplayName("Should verify isGreaterThanOrEqualTo")
    @Test
    void isGreaterThan() {
        assertFalse(Money.ZERO.isGreaterThan(Money.ZERO));
        assertTrue(Money.of(300).isGreaterThan(Money.of(200)));
    }

    @DisplayName("Should sum a new Money")
    @Test
    void plus() {
        assertEquals(Money.of(400), Money.of(100).plus(Money.of(300)));
        assertEquals(Money.of(300), Money.of(0).plus(Money.of(300)));
        assertEquals(Money.of(100), Money.of(100).plus(Money.of(0)));
    }

    @DisplayName("Should minus a new Money")
    @Test
    void minus() {
        assertEquals(Money.of(-200), Money.of(100).minus(Money.of(300)));
        assertEquals(Money.of(-300), Money.of(0).minus(Money.of(300)));
        assertEquals(Money.of(100), Money.of(100).minus(Money.of(0)));
        assertEquals(Money.of(50), Money.of(100).minus(Money.of(50)));
    }

    @DisplayName("Should negate the money")
    @Test
    void negate() {
        assertEquals(Money.ZERO, Money.ZERO.negate());
        assertEquals(Money.of(-150), Money.of(150).negate());
        assertEquals(Money.of(150), Money.of(-150).negate());
    }
}
