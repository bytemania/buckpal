package com.example.buckpal.account.domain.fixture;

class Util {
    static long rand() {
        long lower = 0;
        long upper = 100;
        return (long) (Math.random() * (upper - lower)) + lower;
    }
}
