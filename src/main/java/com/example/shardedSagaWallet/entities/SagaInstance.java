package com.example.shardedSagaWallet.entities;

import jakarta.persistence.*;
import lombok.*;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "saga_instance")
@Builder
public class SagaInstance {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private SagaStatus status = SagaStatus.STARTED;

    @Column(name = "context", columnDefinition = "json")
    private String context;

    @Column(name = "current_step")
    private String currentStep;

    public void markAsStarted() {
        this.status = SagaStatus.STARTED;
    }

    public void markAsRunning() {
        this.status = SagaStatus.RUNNING;
    }

    public void markAsCompleted() {
        this.status = SagaStatus.COMPLETED;
    }

    public void markAsFailed() {
        this.status = SagaStatus.FAILED;
    }

    public void markAsCompensating() {
        this.status = SagaStatus.COMPENSATING;
    }

    public void markAsCompensated() {
        this.status = SagaStatus.COMPENSATED;
    }

}
