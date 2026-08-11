package com.example.shardedSagaWallet.service.saga.steps;


import com.example.shardedSagaWallet.entities.SagaInstance;
import com.example.shardedSagaWallet.entities.Wallet;
import com.example.shardedSagaWallet.repository.SagaInstanceRepository;
import com.example.shardedSagaWallet.repository.WalletRepository;
import com.example.shardedSagaWallet.service.saga.SagaContext;
import com.example.shardedSagaWallet.service.saga.SagaStepInterface;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
@Slf4j
public class DebitSourceWalletStep implements SagaStepInterface {

    private final WalletRepository walletRepository;
    private final Wallet wallet;

    @Override
    @Transactional
    public boolean execute(SagaContext context) {
        Long fromWalletId = context.getLong("fromWalletId");
        BigDecimal amount = context.getBigDecimal("amount");

        log.info("Debiting source wallet {} with amount {}", fromWalletId, amount);

        Wallet wallet = walletRepository.findByIdWithLock(fromWalletId)
                .orElseThrow(() -> new RuntimeException("Wallet not found"));

        log.info("Wallet fetched with balance {}", wallet.getBalance());
        context.put("originalSourceWalletBalance", wallet.getBalance());

        if (!wallet.hasSufficientBalance(amount)) {
            log.info("Insufficient balance in source wallet {}. Current balance: {}, required amount: {}", fromWalletId, wallet.getBalance(), amount);
            throw new RuntimeException("Insufficient balance in source wallet");
        }

        walletRepository.updateBalanceByUserId(fromWalletId, wallet.getBalance().subtract(amount));

        log.info("Wallet saved with balance {}", wallet.getBalance());
        context.put("sourceWalletBalanceAfterDebit", wallet.getBalance());

        log.info("Debit source wallet step executed successfully");


        return true;


    }

    @Override
    @Transactional
    public boolean compensate(SagaContext context) {
        Long fromWalletId = context.getLong("fromWalletId");
        BigDecimal amount = context.getBigDecimal("amount");

        log.info("Compensating source wallet {} with amount {}", fromWalletId, amount);

        Wallet wallet = walletRepository.findByIdWithLock(fromWalletId)
                .orElseThrow(() -> new RuntimeException("Wallet not found"));

        log.info("Wallet fetched with balance {}", wallet.getBalance());
        context.put("sourceWalletBalanceBeforeCreditCompensation", wallet.getBalance());


        walletRepository.updateBalanceByUserId(fromWalletId, wallet.getBalance().add(amount));

        log.info("Wallet saved with balance {}", wallet.getBalance());
        context.put("sourceWalletBalanceAfterCreditCompensation", wallet.getBalance());

        log.info("Compensating source wallet step executed successfully");


        return true;
    }

    @Override
    public String getStepName() {
        return SagaStepFactory.SagaStepType.DEBIT_SOURCE_WALLET_STEP.toString();
    }
}
