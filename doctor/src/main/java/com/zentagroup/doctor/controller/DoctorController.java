package com.zentagroup.doctor.controller;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.zentagroup.doctor.imp.DoctorImp;
import com.zentagroup.doctor.model.Doctor;
import com.zentagroup.doctor.service.IDoctorService;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
public class DoctorController {
    private IDoctorService getDoctor(Doctor doctor) {
        return new DoctorImp(doctor);
    }

    @GetMapping("/doctor/{id}")
    public Doctor retrieveDoctor (@PathVariable(name = "id") int doctorId) {
        Doctor localDoctor = new Doctor(doctorId, null, null);
        return getDoctor(localDoctor).retrieve();
    }

    @RequestMapping(value = "/doctor", method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public Doctor createDoctor (Doctor doctor) {
        return getDoctor(doctor).create();
    }

    @RequestMapping(value = "/doctor", method = RequestMethod.PUT,
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public Doctor updateDoctor (Doctor doctor) {
        return getDoctor(doctor).update();
    }

    @DeleteMapping("/doctor/{id}")
    public Doctor deleteDoctor (@PathVariable(name = "id") int doctorId) {
        Doctor localDoctor = new Doctor(doctorId, null, null);
        Doctor responseDoctor = getDoctor(localDoctor).delete();
        if (responseDoctor == null) {
            return null;
        }
        return responseDoctor;
    }

    @GetMapping("/doctor/{id}/appointment")
    public Map<String, Integer> retrieveAppointments (@PathVariable(name = "id") int doctorId) {
        Doctor localDoctor = new Doctor(doctorId, null, null);
        return getDoctor(localDoctor).findAppointments();
    }

    @GetMapping("/doctor/{id}/patient")
    public List<Integer> retrievePatients (@PathVariable(name = "id") int doctorId) {
        Doctor localDoctor = new Doctor(doctorId, null, null);
        return getDoctor(localDoctor).findPatientsIds();
    }

    @GetMapping("/doctor/patient/{id}/appointment")
    public Map<String, Doctor> retrieveAppointmentsByPatient (@PathVariable(name = "id") int patientId) {
        return getDoctor(new Doctor()).findAppointmentsByPatiend(patientId);
    }

    @GetMapping("/doctor/patient/{id}/doctor")
    public List<Doctor> retrieveDoctorsByPatient (@PathVariable(name = "id") int patientId) {
        return getDoctor(new Doctor()).findDoctorsByPatient(patientId);
    }

    @RequestMapping(value = "/doctor/{id}/patient/{idp}", method = RequestMethod.POST,
    consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public Map <String, Doctor> createAppointment (@PathVariable(name = "id") int doctorId,
                                                   @PathVariable(name = "idp") int patientId, 
                                                   String date) {
        Doctor localDoctor = new Doctor(doctorId, null, null);
        return getDoctor(localDoctor).createAppointment(patientId, date);
    }

}
