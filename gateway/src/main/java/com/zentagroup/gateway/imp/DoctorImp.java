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

    /**
     * Sends a POST HttpRequest to an url corresponding to the Doctor service in order to insert
     * a doctors data into ist database. It sends the request in x-www-form-urlencoded. It receives
     * a response from the Doctor service as a JSON which is mapped into a Doctor object.
     * @param requestBody - Doctor
     * @return CompletableFuture<Doctor>
     */
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

    /**
     * Sends a GET HttpRequest to an url corresponding to the Doctor service in order to retrieve
     * a doctors data from ist database based on an id. It receives a response from the Doctor service
     * as a JSON which is mapped into a Doctor object.
     * @param id - Doctor
     * @return CompletableFuture<Doctor>
     */
    @Override
    public CompletableFuture<Doctor> retrieve(int id) {
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create(DOCTOR_URL + "/" + id))
                .build();

        return httpClient.sendAsync(request, responseInfo -> new DoctorSubscriber())
                .thenApply(HttpResponse::body);
    }

    /**
     * Sends a PUT HttpRequest to an url corresponding to the Doctor service in order to update
     * a doctors data from ist database. It sends the request in x-www-form-urlencoded. It receives
     * a response from the Doctor service as a JSON which is mapped into a Doctor object.
     * @param requestBody - Doctor
     * @return CompletableFuture<Doctor>
     */
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

    /**
     * Retrieves a Doctor and a list of its patients ids from the Doctor service. It then retrieves
     * each patient from the Patient service and returns them as a list in a EntityList object with the Doctor.
     * (Future improvement: forward the list to the patients service and receive a patient list in
     * order to only make one call)
     * @param idDoctor - Doctors id
     * @return EntityList<Doctor, Patient>
     */
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

    /**
     * Returns a doctor and a Map of appointments including the date and the Patient.
     * @param idDoctor
     * @return AppointmentList<Doctor, Patient> - object with a Doctor and a Map of dates and patients.
     */
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

    /**
     * Returns a list of the doctors the patient has had appointments with.
     * Doctor data is retrieved from the Doctor Service and Patient data
     * from the Patient service.
     * @param idPatient
     * @return EntityList<Patient, Doctor> - Patient and list of Doctors
     */
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

    /**
     * Returns a patient and a Map of appointments including the date and the doctor.
     * @param idPatient
     * @return AppointmentList<Patient, Doctor> - Patient and Map of dates and Doctors.
     */
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

    /**
     * It registers a new appointment in the database of the Doctor service based on a date,
     * a Patients id and a Doctors id.
     * @param idDoctor
     * @param idPatient
     * @param date
     * @return Appointment
     */
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
