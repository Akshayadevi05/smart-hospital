package com.example.smarthospitalqueue.repository;

import com.example.smarthospitalqueue.model.Department;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DepartmentRepository extends JpaRepository<Department, Long> {

}