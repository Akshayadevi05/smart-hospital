package com.example.smarthospitalqueue.service;

import com.example.smarthospitalqueue.exception.ResourceNotFoundException;
import com.example.smarthospitalqueue.model.Patient;
import com.example.smarthospitalqueue.repository.PatientRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PatientService {

    private final PatientRepository patientRepository;

    public PatientService(PatientRepository patientRepository) {
        this.patientRepository = patientRepository;
    }

    public Patient addPatient(Patient patient) {
        validatePatient(patient);
        return patientRepository.save(patient);
    }

    public List<Patient> getAllPatients() {
        return patientRepository.findAll();
    }

    public Optional<Patient> getPatientById(Long id) {
        return patientRepository.findById(id);
    }

    public Patient updatePatient(Long id, Patient updatedPatient) {

        Patient existingPatient = patientRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Patient not found with id: " + id));

        validatePatient(updatedPatient);

        existingPatient.setName(updatedPatient.getName());
        existingPatient.setAge(updatedPatient.getAge());
        existingPatient.setGender(updatedPatient.getGender());
        existingPatient.setPhone(updatedPatient.getPhone());
        existingPatient.setEmail(updatedPatient.getEmail());
        existingPatient.setAddress(updatedPatient.getAddress());
        existingPatient.setEmergencyContact(updatedPatient.getEmergencyContact());

        return patientRepository.save(existingPatient);
    }

    public void deletePatient(Long id) {

        if (!patientRepository.existsById(id)) {
            throw new ResourceNotFoundException(
                    "Patient not found with id: " + id);
        }

        patientRepository.deleteById(id);
    }

    private void validatePatient(Patient patient) {

        if (patient.getName() == null || patient.getName().trim().isEmpty()) {
            throw new RuntimeException("Patient name is required");
        }

        if (patient.getAge() <= 0 || patient.getAge() > 120) {
            throw new RuntimeException("Age must be between 1 and 120");
        }

        if (patient.getGender() == null || patient.getGender().trim().isEmpty()) {
            throw new RuntimeException("Gender is required");
        }

        if (patient.getPhone() == null || !patient.getPhone().matches("\\d{10}")) {
            throw new RuntimeException("Phone number must contain 10 digits");
        }

        if (patient.getEmail() != null && !patient.getEmail().trim().isEmpty()) {
            if (!patient.getEmail().matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
                throw new RuntimeException("Invalid email format");
            }
        }
    }
}