package com.example.smarthospitalqueue.repository;

import com.example.smarthospitalqueue.model.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DoctorRepository extends JpaRepository<Doctor, Long> {
}