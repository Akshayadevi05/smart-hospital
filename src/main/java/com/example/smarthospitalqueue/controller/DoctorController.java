package com.example.smarthospitalqueue.controller;

import com.example.smarthospitalqueue.model.Doctor;
import com.example.smarthospitalqueue.service.DoctorService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/doctors")
@CrossOrigin(origins = "http://localhost:5173")
public class DoctorController {

    private final DoctorService doctorService;

    public DoctorController(DoctorService doctorService) {
        this.doctorService = doctorService;
    }

    @GetMapping
    public List<Doctor> getAllDoctors() {
        return doctorService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Doctor> getDoctorById(@PathVariable Long id) {
        return doctorService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Doctor createDoctor(@RequestBody Doctor doctor) {
        return doctorService.save(doctor);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Doctor> updateDoctor(
            @PathVariable Long id,
            @RequestBody Doctor doctor) {

        return doctorService.findById(id)
                .map(existingDoctor -> {
                    existingDoctor.setName(doctor.getName());
                    existingDoctor.setSpecialization(doctor.getSpecialization());
                    existingDoctor.setDepartment(doctor.getDepartment());
                    existingDoctor.setAvailableTime(doctor.getAvailableTime());

                    return ResponseEntity.ok(doctorService.save(existingDoctor));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDoctor(@PathVariable Long id) {

        if (doctorService.findById(id).isPresent()) {
            doctorService.deleteById(id);
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.notFound().build();
    }
}