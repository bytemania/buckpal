package org.example.buckpal.account.service.property;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.buckpal.account.domain.Money;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MoneyTransferProperties {
    private Money maximumTransferThreshold = Money.of(1_000_000L);
}
