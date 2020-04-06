package com.zentagroup.gateway.controller;

import com.zentagroup.gateway.dto.*;
import com.zentagroup.gateway.imp.DoctorImp;
import com.zentagroup.gateway.imp.PatientImp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
@CrossOrigin(origins="*")
@RequestMapping("/api/v1/gateway")
public class DoctorController {
    @Autowired
    DoctorImp doctorImp = new DoctorImp();

    @GetMapping("/doctor/{id}")
    public Doctor retrieveDoctor(@PathVariable(name = "id") int doctorId) {
        return doctorImp.retrieve(doctorId).join();
    }

    @PostMapping("/doctor")
    public Doctor createDoctor(@RequestBody Doctor doctor) {
        return doctorImp.create(doctor).join();
    }

    @PutMapping("/doctor")
    public Doctor updateDoctor(@RequestBody Doctor doctor) {
        return doctorImp.update(doctor).join();
    }

    @DeleteMapping("/doctor/{id}")
    public Doctor deleteDoctor(@PathVariable(name = "id") int doctorId) {
        return doctorImp.delete(doctorId).join();
    }

    @GetMapping("/doctor/{id}/appointment")
    public AppointmentList<Doctor, Patient> retrieveAppointments(@PathVariable(name = "id") int doctorId) {
        return doctorImp.findAppointments(doctorId);
    }

    @GetMapping("/doctor/{id}/patient")
    public EntityList<Doctor, Patient> retrievePatients(@PathVariable(name = "id") int doctorId) {
        return doctorImp.findPatients(doctorId);
    }

    @GetMapping("/doctor/patient/{id}/appointment")
    public AppointmentList<Patient, Doctor> retrieveAppointmentsByPatient(@PathVariable(name = "id") int patientId) {
        return doctorImp.findAppointmentsByPatient(patientId);
    }

    @GetMapping("/doctor/patient/{id}/doctor")
    public EntityList<Patient, Doctor> retrieveDoctorsByPatient(@PathVariable(name = "id") int patientId) {
        return doctorImp.findDoctorsByPatient(patientId);
    }

    @GetMapping("/doctor/{id}/patient/{idp}/appointment")
    public Appointment createAppointment(@PathVariable(name = "id") int doctorId,
                                                @PathVariable(name = "idp") int patientId,
                                                @RequestParam String date) {
        return doctorImp.createAppointment(doctorId, patientId, date);
    }

}
