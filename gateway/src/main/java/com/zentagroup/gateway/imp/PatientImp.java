package com.zentagroup.gateway.imp;

import com.zentagroup.gateway.dto.Patient;
import com.zentagroup.gateway.mapping.HttpMapping;
import com.zentagroup.gateway.service.IPatientService;
import com.zentagroup.gateway.subscriber.PatientSubscriber;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.*;
import java.util.concurrent.CompletableFuture;

import static com.zentagroup.gateway.util.Constant.PATIENT_URL;

@Service
public class PatientImp implements IPatientService {

    private final HttpClient httpClient = HttpClient.newHttpClient();

    @Autowired
    HttpMapping httpMapping = new HttpMapping();

    @Override
    public CompletableFuture<Patient> create(Patient requestBody) {
        Map<Object, Object> data = new HashMap<>();
        data.put("name", requestBody.getName());
        data.put("phone", requestBody.getPhoneNumber());
        data.put("address", requestBody.getAddress());

        HttpRequest request = HttpRequest.newBuilder()
                .POST(httpMapping.buildFormDataFromMap(data))
                .uri(URI.create(PATIENT_URL))
                .setHeader("User-Agent", "Java 11 HttpClient Bot")
                .header("Content-Type", "application/x-www-form-urlencoded")
                .build();

        return httpClient.sendAsync(request, responseInfo -> new PatientSubscriber())
                .thenApply(HttpResponse::body);
    }

    @Override
    public CompletableFuture<Patient> retrieve(int id){
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create(PATIENT_URL + "/" + id))
                .build();

        return httpClient.sendAsync(request, responseInfo -> new PatientSubscriber())
                .thenApply(HttpResponse::body);
    }

    @Override
    public CompletableFuture<Patient> update(Patient requestBody) {
        Map<Object, Object> data = new HashMap<>();
        data.put("id", requestBody.getId());
        data.put("name", requestBody.getName());
        data.put("phone", requestBody.getPhoneNumber());
        data.put("address", requestBody.getAddress());

        HttpRequest request = HttpRequest.newBuilder()
                .PUT(httpMapping.buildFormDataFromMap(data))
                .uri(URI.create(PATIENT_URL))
                .setHeader("User-Agent", "Java 11 HttpClient Bot")
                .header("Content-Type", "application/x-www-form-urlencoded")
                .build();

        return httpClient.sendAsync(request, responseInfo -> new PatientSubscriber())
                .thenApply(HttpResponse::body);
    }

    @Override
    public CompletableFuture<Patient> delete(int id) {
        /**
        HttpRequest request = HttpRequest.newBuilder()
                .DELETE()
                .uri(URI.create(url + "/" + id))
                .build();

        return httpClient.sendAsync(request, responseInfo -> new PatientSubscriber())
                .thenApply(HttpResponse::body);
         **/
        return null;
    }

}
