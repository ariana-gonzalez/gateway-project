package com.zentagroup.doctor.service;

import java.util.List;
import java.util.Map;

import com.zentagroup.doctor.model.Doctor;
import com.zentagroup.doctor.repository.ICrudRepository;

public interface IDoctorService extends ICrudRepository<Doctor> {
    List<Integer> findPatientsIds ();
    Map <String, Integer> findAppointments ();
    List<Doctor> findDoctorsByPatient(int patientId);
    Map <String, Doctor> findAppointmentsByPatiend(int patientId);
    Map<String, Doctor> createAppointment (int patientId, String date);
}
