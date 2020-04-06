package com.zentagroup.gateway.service;

import com.zentagroup.gateway.dto.*;
import com.zentagroup.gateway.repository.ICrudRepository;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface IDoctorService extends ICrudRepository<Doctor> {
    EntityList<Doctor, Patient> findPatients (int idDoctor);
    AppointmentList<Doctor, Patient> findAppointments (int idDoctor);

    EntityList<Patient, Doctor> findDoctorsByPatient (int idPatient);
    AppointmentList<Patient, Doctor> findAppointmentsByPatient (int idPatient);

    Appointment createAppointment (int idDoctor, int idPatient, String date);


}
