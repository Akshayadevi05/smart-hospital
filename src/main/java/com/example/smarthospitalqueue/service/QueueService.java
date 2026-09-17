package com.example.smarthospitalqueue.service;

import com.example.smarthospitalqueue.dto.QueueRequest;
import com.example.smarthospitalqueue.exception.ResourceNotFoundException;
import com.example.smarthospitalqueue.model.*;
import com.example.smarthospitalqueue.repository.DepartmentRepository;
import com.example.smarthospitalqueue.repository.DoctorRepository;
import com.example.smarthospitalqueue.repository.PatientRepository;
import com.example.smarthospitalqueue.repository.QueueRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

@Service
public class QueueService {

    private final QueueRepository queueRepository;
    private final PatientRepository patientRepository;
    private final DoctorRepository doctorRepository;
    private final DepartmentRepository departmentRepository;

    public QueueService(
            QueueRepository queueRepository,
            PatientRepository patientRepository,
            DoctorRepository doctorRepository,
            DepartmentRepository departmentRepository) {

        this.queueRepository = queueRepository;
        this.patientRepository = patientRepository;
        this.doctorRepository = doctorRepository;
        this.departmentRepository = departmentRepository;
    }

    public List<HospitalQueue> findAll() {
        return queueRepository.findAll();
    }

    public List<HospitalQueue> findQueuesByPriority() {

        LocalDate today = LocalDate.now();

        return queueRepository.findQueuesByPriority(today);
    }

    public List<HospitalQueue> findQueuesByStatus(QueueStatus status) {

        return queueRepository.findByStatus(status);
    }

    public List<HospitalQueue> findTodaysQueues() {

        LocalDate today = LocalDate.now();

        return queueRepository.findByQueueDate(today);
    }

    public Map<String, Long> getTodaySummary() {

        List<HospitalQueue> queues = findTodaysQueues();

        Map<String, Long> summary = new HashMap<>();

        summary.put("total", (long) queues.size());

        summary.put("waiting",
                queues.stream()
                        .filter(q -> q.getStatus() == QueueStatus.WAITING)
                        .count());

        summary.put("serving",
                queues.stream()
                        .filter(q -> q.getStatus() == QueueStatus.SERVING)
                        .count());

        summary.put("completed",
                queues.stream()
                        .filter(q -> q.getStatus() == QueueStatus.COMPLETED)
                        .count());

        summary.put("cancelled",
                queues.stream()
                        .filter(q -> q.getStatus() == QueueStatus.CANCELLED)
                        .count());

        return summary;
    }

    public HospitalQueue findNextPatient() {

        List<HospitalQueue> queues = findQueuesByPriority();

        if (queues.isEmpty()) {
            return null;
        }

        return queues.get(0);
    }

    public HospitalQueue serveNextPatient() {

        HospitalQueue nextPatient = findNextPatient();

        if (nextPatient == null) {
            return null;
        }

        nextPatient.setStatus(QueueStatus.SERVING);

        return queueRepository.save(nextPatient);
    }

    public HospitalQueue completeQueue(Long id) {

        HospitalQueue queue = queueRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Queue not found with id: " + id));

        if (queue.getStatus() != QueueStatus.SERVING) {
            throw new RuntimeException(
                    "Only a serving patient can be completed");
        }

        queue.setStatus(QueueStatus.COMPLETED);

        return queueRepository.save(queue);
    }

    public HospitalQueue cancelQueue(Long id) {

        HospitalQueue queue = queueRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Queue not found with id: " + id));

        if (queue.getStatus() != QueueStatus.WAITING) {
            throw new RuntimeException(
                    "Only a waiting patient can be cancelled");
        }

        queue.setStatus(QueueStatus.CANCELLED);

        return queueRepository.save(queue);
    }

    public HospitalQueue findById(Long id) {

        return queueRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Queue not found with id: " + id));
    }

    public HospitalQueue createQueue(QueueRequest request) {

        validateQueueRequest(request);

        Patient patient = patientRepository.findById(request.getPatientId())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Patient not found with id: "
                                        + request.getPatientId()));

        Doctor doctor = doctorRepository.findById(request.getDoctorId())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Doctor not found with id: "
                                        + request.getDoctorId()));

        Department department =
                departmentRepository.findById(request.getDepartmentId())
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Department not found with id: "
                                                + request.getDepartmentId()));

        LocalDate today = LocalDate.now();

        Integer maxQueueNumber =
                queueRepository.findMaxQueueNumberByDoctorAndDate(
                        doctor.getId(),
                        today
                );

        Integer nextQueueNumber;

        if (maxQueueNumber == null) {
            nextQueueNumber = 1;
        } else {
            nextQueueNumber = maxQueueNumber + 1;
        }

        HospitalQueue hospitalQueue = new HospitalQueue();

        hospitalQueue.setPatient(patient);
        hospitalQueue.setDoctor(doctor);
        hospitalQueue.setDepartment(department);

        hospitalQueue.setQueueNumber(nextQueueNumber);
        hospitalQueue.setQueueDate(today);

        hospitalQueue.setStatus(request.getStatus());

        if (request.getPriority() == null) {
            hospitalQueue.setPriority(QueuePriority.NORMAL);
        } else {
            hospitalQueue.setPriority(request.getPriority());
        }

        return queueRepository.save(hospitalQueue);
    }

    public HospitalQueue updateQueue(Long id, QueueRequest request) {

        HospitalQueue existingQueue = queueRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Queue not found with id: " + id));

        validateQueueRequest(request);

        Patient patient = patientRepository.findById(request.getPatientId())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Patient not found with id: "
                                        + request.getPatientId()));

        Doctor doctor = doctorRepository.findById(request.getDoctorId())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Doctor not found with id: "
                                        + request.getDoctorId()));

        Department department =
                departmentRepository.findById(request.getDepartmentId())
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Department not found with id: "
                                                + request.getDepartmentId()));

        existingQueue.setPatient(patient);
        existingQueue.setDoctor(doctor);
        existingQueue.setDepartment(department);
        existingQueue.setStatus(request.getStatus());
        existingQueue.setPriority(request.getPriority());

        return queueRepository.save(existingQueue);
    }

    public void deleteById(Long id) {

        if (!queueRepository.existsById(id)) {
            throw new ResourceNotFoundException(
                    "Queue not found with id: " + id);
        }

        queueRepository.deleteById(id);
    }

    private void validateQueueRequest(QueueRequest request) {

        if (request.getPatientId() == null ||
                request.getPatientId() <= 0) {
            throw new RuntimeException("Patient is required");
        }

        if (request.getDoctorId() == null ||
                request.getDoctorId() <= 0) {
            throw new RuntimeException("Doctor is required");
        }

        if (request.getDepartmentId() == null ||
                request.getDepartmentId() <= 0) {
            throw new RuntimeException("Department is required");
        }

        if (request.getStatus() == null) {
            throw new RuntimeException("Queue status is required");
        }
    }
}