package com.example.shardedSagaWallet.service;

import com.example.shardedSagaWallet.entities.Wallet;
import com.example.shardedSagaWallet.repository.WalletRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;


@Slf4j
@Service
@RequiredArgsConstructor
public class WalletService {
    private final WalletRepository walletRepository;

    public Wallet createWallet(Long userId) {
        log.info("Creating wallet for user {}", userId);
        Wallet wallet = Wallet.builder()
                .userId(userId)
                .isActive(true)
                .balance(BigDecimal.ZERO)
                .build();
        wallet = walletRepository.save(wallet);
        log.info("Wallet created with id {}", wallet.getId());
        return wallet;
    }

    public Wallet getWalletById(Long id) {
        return walletRepository.findById(id).orElseThrow(() -> new RuntimeException("Wallet not found"));
    }

    public List<Wallet> getWalletsByUserId(Long userId) {

        return walletRepository.findByUserId(userId);
    }

    @Transactional
    public Wallet debit(Long userId, BigDecimal amount) {
        log.info("Debiting {} from wallet {}", amount, userId);
        Wallet wallet = getWalletByUserId(userId);
        walletRepository.updateBalanceByUserId(userId, wallet.getBalance().subtract(amount));
        log.info("Debit successful for wallet {}", wallet.getId());
        return walletRepository.save(wallet);

    }

    @Transactional
    public Wallet credit(Long userId, BigDecimal amount) {
        log.info("Crediting {} to wallet {}", amount, userId);
        Wallet wallet = getWalletByUserId(userId);
        walletRepository.updateBalanceByUserId(userId, wallet.getBalance().add(amount));
        log.info("Credit successful for wallet {}", wallet.getId());
        return walletRepository.save(wallet);

    }
    public Wallet getWalletByUserId(Long userId) {
        log.info("Getting wallet by user id {}", userId);
        return walletRepository.findByUserId(userId).get(0);
    }

    public BigDecimal getWalletBalance(Long walletId) {
        log.info("Getting balance for wallet {}", walletId);
        BigDecimal balance = getWalletById(walletId).getBalance();
        log.info("Balance for wallet {} is {}", walletId, balance);
        return balance;
    }
}
