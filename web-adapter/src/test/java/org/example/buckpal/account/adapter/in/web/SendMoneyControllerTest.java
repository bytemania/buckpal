package org.example.buckpal.account.adapter.in.web;

import org.example.buckpal.account.domain.Account;
import org.example.buckpal.account.domain.Money;
import org.example.buckpal.account.port.in.SendMoneyCommand;
import org.example.buckpal.account.port.in.SendMoneyUseCase;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.then;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(controllers = SendMoneyController.class)
class SendMoneyControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SendMoneyUseCase sendMoneyUseCase;

    @DisplayName("The controller should create a command and call the sendmoney usecase")
    @Test
    void testSendMoney() throws Exception {
        mockMvc.perform(post("/accounts/send/{sourceAccountId}/{targetAccountId}/{amount}", 41L, 42L, 500.0)
                .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk());

        SendMoneyCommand expectedCommand = new SendMoneyCommand(Account.AccountId.of(41L), Account.AccountId.of(42L), Money.of(500.0));
        then(sendMoneyUseCase).should().sendMoney(eq(expectedCommand));
    }

}