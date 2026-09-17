package com.example.smarthospitalqueue.controller;

import com.example.smarthospitalqueue.dto.QueueRequest;
import com.example.smarthospitalqueue.model.HospitalQueue;
import com.example.smarthospitalqueue.model.QueueStatus;
import com.example.smarthospitalqueue.service.QueueService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin(origins = "http://localhost:5173")
@RequestMapping("/api/queue")
public class QueueController {

    private final QueueService queueService;

    public QueueController(QueueService queueService) {
        this.queueService = queueService;
    }

    @GetMapping
    public List<HospitalQueue> getAllQueues() {
        return queueService.findAll();
    }

    // Get today's queues based on priority
    @GetMapping("/priority")
    public List<HospitalQueue> getQueuesByPriority() {
        return queueService.findQueuesByPriority();
    }

    @GetMapping("/status/{status}")
    public List<HospitalQueue> getQueuesByStatus(
            @PathVariable QueueStatus status) {

        return queueService.findQueuesByStatus(status);
    }

    @GetMapping("/next")
    public ResponseEntity<HospitalQueue> getNextPatient() {

        HospitalQueue nextPatient = queueService.findNextPatient();

        if (nextPatient == null) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(nextPatient);
    }

    @PostMapping("/serve-next")
    public ResponseEntity<HospitalQueue> serveNextPatient() {

        HospitalQueue servedPatient = queueService.serveNextPatient();

        if (servedPatient == null) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(servedPatient);
    }

    @PutMapping("/{id}/complete")
    public ResponseEntity<HospitalQueue> completeQueue(@PathVariable Long id) {

        try {
            HospitalQueue completedQueue = queueService.completeQueue(id);

            return ResponseEntity.ok(completedQueue);

        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }


    }

    @PutMapping("/{id}/cancel")
    public ResponseEntity<HospitalQueue> cancelQueue(@PathVariable Long id) {

        try {
            HospitalQueue cancelledQueue = queueService.cancelQueue(id);

            return ResponseEntity.ok(cancelledQueue);

        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<HospitalQueue> getQueueById(@PathVariable Long id) {

        HospitalQueue queue = queueService.findById(id);

        if (queue == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(queue);
    }

    @PostMapping
    public HospitalQueue createQueue(@RequestBody QueueRequest request) {
        return queueService.createQueue(request);
    }

    @PutMapping("/{id}")
    public ResponseEntity<HospitalQueue> updateQueue(
            @PathVariable Long id,
            @RequestBody QueueRequest request) {

        HospitalQueue existingQueue = queueService.findById(id);

        if (existingQueue == null) {
            return ResponseEntity.notFound().build();
        }

        HospitalQueue updatedQueue = queueService.updateQueue(id, request);

        return ResponseEntity.ok(updatedQueue);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteQueue(@PathVariable Long id) {

        HospitalQueue existingQueue = queueService.findById(id);

        if (existingQueue == null) {
            return ResponseEntity.notFound().build();
        }

        queueService.deleteById(id);

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/today")
    public List<HospitalQueue> getTodaysQueues() {

        return queueService.findTodaysQueues();
    }

    @GetMapping("/summary")
    public Map<String, Long> getTodaySummary() {

        return queueService.getTodaySummary();
    }
}