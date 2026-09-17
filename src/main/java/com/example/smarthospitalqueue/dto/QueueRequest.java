package com.example.smarthospitalqueue.dto;

import com.example.smarthospitalqueue.model.QueuePriority;
import com.example.smarthospitalqueue.model.QueueStatus;

public class QueueRequest {

    private Long patientId;
    private Long doctorId;
    private Long departmentId;
    private QueueStatus status;
    private QueuePriority priority;

    public QueueRequest() {
    }

    public Long getPatientId() {
        return patientId;
    }

    public void setPatientId(Long patientId) {
        this.patientId = patientId;
    }

    public Long getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(Long doctorId) {
        this.doctorId = doctorId;
    }

    public Long getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(Long departmentId) {
        this.departmentId = departmentId;
    }

    public QueueStatus getStatus() {
        return status;
    }

    public void setStatus(QueueStatus status) {
        this.status = status;
    }

    public QueuePriority getPriority() {
        return priority;
    }

    public void setPriority(QueuePriority priority) {
        this.priority = priority;
    }
}