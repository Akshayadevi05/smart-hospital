package com.example.smarthospitalqueue.service;

import com.example.smarthospitalqueue.exception.ResourceNotFoundException;
import com.example.smarthospitalqueue.model.Department;
import com.example.smarthospitalqueue.repository.DepartmentRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DepartmentService {

    private final DepartmentRepository departmentRepository;

    public DepartmentService(DepartmentRepository departmentRepository) {
        this.departmentRepository = departmentRepository;
    }

    public Department addDepartment(Department department) {
        validateDepartment(department);
        return departmentRepository.save(department);
    }

    public List<Department> getAllDepartments() {
        return departmentRepository.findAll();
    }

    public Optional<Department> getDepartmentById(Long id) {
        return departmentRepository.findById(id);
    }

    public Department updateDepartment(Long id, Department updatedDepartment) {

        Department existingDepartment = departmentRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Department not found with id: " + id));

        validateDepartment(updatedDepartment);

        existingDepartment.setName(updatedDepartment.getName());
        existingDepartment.setDescription(updatedDepartment.getDescription());
        existingDepartment.setActive(updatedDepartment.isActive());

        return departmentRepository.save(existingDepartment);
    }

    public void deleteDepartment(Long id) {

        if (!departmentRepository.existsById(id)) {
            throw new ResourceNotFoundException(
                    "Department not found with id: " + id);
        }

        departmentRepository.deleteById(id);
    }

    private void validateDepartment(Department department) {

        if (department.getName() == null ||
                department.getName().trim().isEmpty()) {
            throw new RuntimeException("Department name is required");
        }

        if (department.getName().trim().length() < 2) {
            throw new RuntimeException(
                    "Department name must contain at least 2 characters");
        }

        if (department.getDescription() == null ||
                department.getDescription().trim().isEmpty()) {
            throw new RuntimeException(
                    "Department description is required");
        }
    }
}