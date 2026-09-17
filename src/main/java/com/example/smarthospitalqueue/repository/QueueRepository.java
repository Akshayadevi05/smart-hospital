package com.example.smarthospitalqueue.repository;

import com.example.smarthospitalqueue.model.HospitalQueue;
import com.example.smarthospitalqueue.model.QueueStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface QueueRepository extends JpaRepository<HospitalQueue, Long> {

    @Query("""
            SELECT MAX(q.queueNumber)
            FROM HospitalQueue q
            WHERE q.doctor.id = :doctorId
            AND q.queueDate = :queueDate
            """)
    Integer findMaxQueueNumberByDoctorAndDate(
            @Param("doctorId") Long doctorId,
            @Param("queueDate") LocalDate queueDate
    );

    // Get today's queues ordered by priority
    @Query("""
        SELECT q
        FROM HospitalQueue q
        WHERE q.queueDate = :queueDate
        AND q.status = 'WAITING'
        ORDER BY
            CASE
                WHEN q.priority = com.example.smarthospitalqueue.model.QueuePriority.EMERGENCY THEN 1
                WHEN q.priority = com.example.smarthospitalqueue.model.QueuePriority.PRIORITY THEN 2
                WHEN q.priority = com.example.smarthospitalqueue.model.QueuePriority.NORMAL THEN 3
                ELSE 3
            END,
            q.queueNumber ASC
        """)
    List<HospitalQueue> findQueuesByPriority(
            @Param("queueDate") LocalDate queueDate
    );

    List<HospitalQueue> findByStatus(QueueStatus status);
    List<HospitalQueue> findByQueueDate(LocalDate queueDate);
}