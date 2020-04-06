package com.zentagroup.gateway.imp;

import com.zentagroup.gateway.dto.*;
import com.zentagroup.gateway.mapping.HttpMapping;
import com.zentagroup.gateway.service.IDoctorService;
import com.zentagroup.gateway.subscriber.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.*;
import java.util.concurrent.CompletableFuture;

import static com.zentagroup.gateway.util.Constant.*;

@Service
public class DoctorImp implements IDoctorService {

    private final HttpClient httpClient = HttpClient.newHttpClient();

    @Autowired
    PatientImp patientImp = new PatientImp();

    @Autowired
    HttpMapping httpMapping = new HttpMapping();

    @Override
    public CompletableFuture<Doctor> create(Doctor requestBody) {
        Map<Object, Object> data = new HashMap<>();
        data.put("name", requestBody.getName());
        data.put("specialization", requestBody.getSpecialization());

        HttpRequest request = HttpRequest.newBuilder()
                .POST(httpMapping.buildFormDataFromMap(data))
                .uri(URI.create(DOCTOR_URL))
                .setHeader("User-Agent", "Java 11 HttpClient Bot")
                .header("Content-Type", "application/x-www-form-urlencoded")
                .build();

        return httpClient.sendAsync(request, responseInfo -> new DoctorSubscriber())
                .thenApply(HttpResponse::body);
    }

    @Override
    public CompletableFuture<Doctor> retrieve(int id) {
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create(DOCTOR_URL + "/" + id))
                .build();

        return httpClient.sendAsync(request, responseInfo -> new DoctorSubscriber())
                .thenApply(HttpResponse::body);
    }

    @Override
    public CompletableFuture<Doctor> update(Doctor requestBody) {
        Map<Object, Object> data = new HashMap<>();
        data.put("id", requestBody.getId());
        data.put("name", requestBody.getName());
        data.put("specialization", requestBody.getSpecialization());

        HttpRequest request = HttpRequest.newBuilder()
                .PUT(httpMapping.buildFormDataFromMap(data))
                .uri(URI.create(DOCTOR_URL))
                .setHeader("User-Agent", "Java 11 HttpClient Bot")
                .header("Content-Type", "application/x-www-form-urlencoded")
                .build();

        return httpClient.sendAsync(request, responseInfo -> new DoctorSubscriber())
                .thenApply(HttpResponse::body);
    }

    @Override
    public CompletableFuture<Doctor> delete(int id) {
        return null;
    }

    @Override
    public EntityList<Doctor, Patient> findPatients(int idDoctor) {
        EntityList<Doctor, Patient> response = new EntityList<>();
        response.setEntity(this.retrieve(idDoctor).join());

        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create(DOCTOR_URL + "/" + idDoctor + "/patient"))
                .build();

        List<Integer> list = httpClient.sendAsync(request, responseInfo -> new ListSubscriber())
                .thenApply(HttpResponse::body).join();

        List<Patient> patients = new ArrayList<>();
        for (Integer idUser : list){
            Patient patient = patientImp.retrieve(idUser).join();
            patients.add(patient);
        }
        response.setList(patients);
        return response;
    }

    @Override
    public AppointmentList<Doctor, Patient> findAppointments(int idDoctor) {
        AppointmentList<Doctor, Patient> response = new AppointmentList<>();
        response.setMainEntity(this.retrieve(idDoctor).join());

        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create(DOCTOR_URL + "/" + idDoctor + "/appointment"))
                .build();

        Map<String, Integer> map = httpClient.sendAsync(request, responseInfo -> new MapSubscriber())
                .thenApply(HttpResponse::body).join();

        HashMap<String, Patient> hm = new HashMap<>();
        for (Map.Entry<String, Integer> entry : map.entrySet()) {
            Patient patient = patientImp.retrieve(entry.getValue()).join();
            String date = entry.getKey();
            hm.put(date, patient);
        }
        response.setAppointments(hm);
        return response;
    }

    //

    @Override
    public EntityList<Patient, Doctor> findDoctorsByPatient (int idPatient) {
        EntityList<Patient, Doctor> response = new EntityList<>();
        response.setEntity(patientImp.retrieve(idPatient).join());

        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create(DOCTOR_URL + "/patient/" + idPatient + "/doctor"))
                .build();

        List<Doctor> list = httpClient.sendAsync(request, responseInfo -> new ListDoctorSubscriber())
                .thenApply(HttpResponse::body).join();

        response.setList(list);
        return response;
    }

    @Override
    public AppointmentList<Patient, Doctor> findAppointmentsByPatient (int idPatient) {
        AppointmentList<Patient, Doctor> response = new AppointmentList<>();
        response.setMainEntity(patientImp.retrieve(idPatient).join());

        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create(DOCTOR_URL + "/patient/" + idPatient + "/appointment"))
                .build();

        Map<String, Doctor> map = httpClient.sendAsync(request, responseInfo -> new MapDoctorSubscriber())
                .thenApply(HttpResponse::body).join();

        response.setAppointments(map);
        return response;
    }

    public Appointment  createAppointment(int idDoctor, int idPatient, String date) {
        Appointment appointment = new Appointment();
        Map<Object, Object> data = new HashMap<>();
        data.put("date", date);

        HttpRequest request = HttpRequest.newBuilder()
                .POST(httpMapping.buildFormDataFromMap(data))
                .uri(URI.create(DOCTOR_URL + "/" + idDoctor + "/patient/" + idPatient))
                .setHeader("User-Agent", "Java 11 HttpClient Bot")
                .header("Content-Type", "application/x-www-form-urlencoded")
                .build();

        Map<String, Doctor> map = httpClient.sendAsync(request, responseInfo -> new MapDoctorSubscriber())
                .thenApply(HttpResponse::body).join();

        Map.Entry<String, Doctor> entry = map.entrySet().iterator().next();
        appointment.setDoctor(entry.getValue());
        appointment.setDate(entry.getKey());
        appointment.setPatient(patientImp.retrieve(idPatient).join());
        return appointment;
    }
}
