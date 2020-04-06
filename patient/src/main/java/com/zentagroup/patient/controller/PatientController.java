package com.zentagroup.patient.controller;

import com.zentagroup.patient.imp.PatientImp;
import com.zentagroup.patient.model.Patient;
import com.zentagroup.patient.service.IPatientService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class PatientController {

    private IPatientService getPatient(Patient patient) {
        return new PatientImp(patient);
    }

    @GetMapping("/patient/{id}")
    public Patient retrievePatient(@PathVariable(name = "id") int patientId) {
        Patient localPatient = new Patient(patientId, null, null, null);
        return getPatient(localPatient).retrieve();
    }

    /**when we use application/x-www-form-urlencoded, Spring doesn't understand it 
     * as a RequestBody. So, if we want to use this we must remove the @RequestBody 
     * annotation.
    **/
    @RequestMapping(value = "/patient", method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public Patient createPatient(Patient patient) {
        return getPatient(patient).create();
    }

    @RequestMapping(value = "/patient", method = RequestMethod.PUT,
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public Patient updatePatient(Patient patient) {
        return getPatient(patient).update();
    }

    @DeleteMapping("/patient/{id}")
    public Patient deletePatient(@PathVariable(name = "id") int patientId) {
        Patient localPatient = new Patient(patientId, null, null, null);
        Patient responsePatient = getPatient(localPatient).delete();
        if (responsePatient == null) {
            return null;
        }
        return responsePatient;
    }

}
