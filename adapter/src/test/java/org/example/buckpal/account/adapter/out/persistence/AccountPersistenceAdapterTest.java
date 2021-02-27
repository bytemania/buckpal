package org.example.buckpal.account.adapter.out.persistence;

import com.example.buckpal.account.domain.fixture.AccountBuilder;
import com.example.buckpal.account.domain.fixture.ActivityBuilder;
import org.example.buckpal.account.domain.Account;
import org.example.buckpal.account.domain.ActivityWindow;
import org.example.buckpal.account.domain.Money;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.jdbc.Sql;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Import({AccountPersistenceAdapter.class, AccountMapper.class})
class AccountPersistenceAdapterTest {

    @Autowired
    private AccountPersistenceAdapter adapter;

    @Autowired
    private ActivityRepository activityRepository;

    @DisplayName("should load the accounts from the database")
    @Test
    @Sql("AccountPersistenceAdapterTest.sql")
    void loadAccount() {
        Account account = adapter.loadAccount(Account.AccountId.of(1L), LocalDateTime.of(2018, 8, 10, 0, 0));

        assertEquals(2, account.getActivityWindow().getActivities().size());
        assertEquals(Money.of(500.0), account.calculateBalance());
    }

    @DisplayName("should update the activities")
    @Test
    void updatesActivities() {
        Account account = AccountBuilder.builder()
                .withBaselineBalance(Money.of(555.0))
                .withActivityWindow(ActivityWindow.create(ActivityBuilder.builder().withEmptyId().withMoney(1L).build()))
                .build();

        adapter.updateActivities(account);

        assertThat(activityRepository.count()).isEqualTo(1);
        assertThat(activityRepository.findAll().get(0).getAmount()).isEqualTo(1);
    }

}