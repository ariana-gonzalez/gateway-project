package com.zentagroup.gateway.service;

import com.zentagroup.gateway.dto.AppointmentList;
import com.zentagroup.gateway.dto.Doctor;
import com.zentagroup.gateway.dto.EntityList;
import com.zentagroup.gateway.dto.Patient;
import com.zentagroup.gateway.repository.ICrudRepository;

import java.util.concurrent.CompletableFuture;

public interface IPatientService extends ICrudRepository<Patient> {
}
