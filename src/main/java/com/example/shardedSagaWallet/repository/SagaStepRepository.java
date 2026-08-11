package com.example.shardedSagaWallet.repository;

import com.example.shardedSagaWallet.entities.SagaStep;
import com.example.shardedSagaWallet.entities.StepStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SagaStepRepository extends JpaRepository<SagaStep, Long> {
    List<SagaStep> findBySagaInstanceId(Long sagaInstanceId);

    List<SagaStep> findBySagaInstanceIdAndStatus(Long sagaInstanceId, StepStatus status);

    Optional<SagaStep> findBySagaInstanceIdAndStepNameAndStatus(Long sagaInstanceId, String stepName, StepStatus status);

    @Query("SELECT s FROM SagaStep s WHERE s.sagaInstanceId = :sagaInstanceId AND s.status = StepStatus. COMPLETED")
    List<SagaStep> findCompletedStepsBySagaInstanceId(@Param("sagaInstanceId") Long sagaInstanceId );

}
