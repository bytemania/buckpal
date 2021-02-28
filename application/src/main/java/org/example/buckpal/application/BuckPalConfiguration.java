package org.example.buckpal.application;

import org.example.buckpal.account.domain.Money;
import org.example.buckpal.account.service.property.MoneyTransferProperties;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@ComponentScan(basePackages = "org.example.buckpal.account")
@EntityScan("org.example.buckpal.account.adapter.out.persistence")
@EnableJpaRepositories("org.example.buckpal.account.adapter.out.persistence")
@EnableTransactionManagement
@EnableConfigurationProperties(BuckPalConfigurationProperties.class)
public class BuckPalConfiguration {
    @Bean
    public MoneyTransferProperties moneyTransferProperties(BuckPalConfigurationProperties buckPalConfigurationProperties) {
        return new MoneyTransferProperties(Money.of(buckPalConfigurationProperties.getTransferThreshold()));
    }
}
