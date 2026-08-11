package com.example.shardedSagaWallet.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Table(name = "transaction")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "from_wallet_id", nullable = false)
    private Long fromWalletId;

    @Column(name = "to_wallet_id", nullable = false)
    private Long toWalletId;

    @Column(name = "amount", nullable = false)
    private BigDecimal amount;

    @Builder.Default
    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private TransactionStatus status = TransactionStatus.PENDING;

    @Builder.Default
    @Enumerated(EnumType.STRING)
    @Column(name = "transaction_type")
    private TransactionType type = TransactionType.TRANSFER;

    @Column(name = "description")
    private String description;

    @Column(name = "saga_instance_id")
    private Long sagaInstanceId;

}
