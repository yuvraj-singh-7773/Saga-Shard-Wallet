package com.example.shardedSagaWallet.config;

import com.example.shardedSagaWallet.service.saga.SagaStepInterface;
import com.example.shardedSagaWallet.service.saga.steps.CreditDestinationWalletStep;
import com.example.shardedSagaWallet.service.saga.steps.DebitSourceWalletStep;
import com.example.shardedSagaWallet.service.saga.steps.SagaStepFactory;
import com.example.shardedSagaWallet.service.saga.steps.UpdateTransactionStatus;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class SagaConfiguration {

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }

    @Bean
    public Map<String, SagaStepInterface> sagaStepMap(
            DebitSourceWalletStep debitSourceWalletStep,
            CreditDestinationWalletStep creditDestinationWalletStep,
            UpdateTransactionStatus updateTransactionStatus
    ) {
        Map<String, SagaStepInterface> map = new HashMap<>();
        map.put(SagaStepFactory.SagaStepType.DEBIT_SOURCE_WALLET_STEP.toString(), debitSourceWalletStep);
        map.put(SagaStepFactory.SagaStepType.CREDIT_DESTINATION_WALLET_STEP.toString(), creditDestinationWalletStep);
        map.put(SagaStepFactory.SagaStepType.UPDATE_TRANSACTION_STATUS_STEP.toString(), updateTransactionStatus);
        return map;
    }
}
