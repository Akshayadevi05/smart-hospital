package com.example.smarthospitalqueue.repository;

import com.example.smarthospitalqueue.model.Patient;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PatientRepository extends JpaRepository<Patient, Long> {

}