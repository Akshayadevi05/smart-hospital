package com.example.smarthospitalqueue.service;

import com.example.smarthospitalqueue.exception.ResourceNotFoundException;
import com.example.smarthospitalqueue.model.Doctor;
import com.example.smarthospitalqueue.repository.DoctorRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DoctorService {

    private final DoctorRepository doctorRepository;

    public DoctorService(DoctorRepository doctorRepository) {
        this.doctorRepository = doctorRepository;
    }

    public List<Doctor> findAll() {
        return doctorRepository.findAll();
    }

    public Optional<Doctor> findById(Long id) {
        return doctorRepository.findById(id);
    }

    public Doctor save(Doctor doctor) {
        validateDoctor(doctor);
        return doctorRepository.save(doctor);
    }

    public void deleteById(Long id) {

        if (!doctorRepository.existsById(id)) {
            throw new ResourceNotFoundException(
                    "Doctor not found with id: " + id);
        }

        doctorRepository.deleteById(id);
    }

    private void validateDoctor(Doctor doctor) {

        if (doctor.getName() == null ||
                doctor.getName().trim().isEmpty()) {
            throw new RuntimeException("Doctor name is required");
        }

        if (doctor.getSpecialization() == null ||
                doctor.getSpecialization().trim().isEmpty()) {
            throw new RuntimeException("Doctor specialization is required");
        }

        if (doctor.getDepartment() == null ||
                doctor.getDepartment().trim().isEmpty()) {
            throw new RuntimeException("Doctor department is required");
        }

        if (doctor.getAvailableTime() == null ||
                doctor.getAvailableTime().trim().isEmpty()) {
            throw new RuntimeException("Doctor available time is required");
        }
    }
}