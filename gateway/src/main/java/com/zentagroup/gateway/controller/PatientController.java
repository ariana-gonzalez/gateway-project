package com.zentagroup.gateway.controller;

import com.zentagroup.gateway.dto.Patient;
import com.zentagroup.gateway.imp.PatientImp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins="*")
@RequestMapping("/api/v1/gateway")
public class PatientController {

    @Autowired
    PatientImp patientImp = new PatientImp();

    @GetMapping("/patient/{id}")
    public Patient retrievePatient(@PathVariable(name = "id") int patientId) {
        return patientImp.retrieve(patientId).join();
    }

    @PostMapping("/patient")
    public Patient createPatient(@RequestBody Patient patient) {
        return patientImp.create(patient).join();
    }

    @PutMapping("/patient")
    public Patient updatePatient(@RequestBody Patient patient) {
        return patientImp.update(patient).join();
    }

    @DeleteMapping("/patient/{id}")
    public Patient deletePatient(@PathVariable(name = "id") int patientId) {
        return null;
    }

}
