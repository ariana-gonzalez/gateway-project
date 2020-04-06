package com.zentagroup.gateway.subscriber;

import com.google.gson.Gson;
import com.zentagroup.gateway.dto.Patient;

import java.net.http.HttpResponse;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.Flow;

public class PatientSubscriber implements HttpResponse.BodySubscriber<Patient> {

    final CompletableFuture<Patient> patientCF = new CompletableFuture<Patient>();
    Flow.Subscription subscription;
    List<ByteBuffer> responseData = new ArrayList<>();

    @Override
    public CompletionStage<Patient> getBody() {
        return patientCF;
    }

    @Override
    public void onSubscribe(Flow.Subscription subscription) {
        this.subscription = subscription;
        subscription.request(1);
    }

    @Override
    public void onNext(List<ByteBuffer> buffers) {
        responseData.addAll(buffers);
        subscription.request(1);
    }

    @Override
    public void onError(Throwable throwable) {
        patientCF.completeExceptionally(throwable);
    }

    @Override
    public void onComplete() {
        int size = responseData.stream().mapToInt(ByteBuffer::remaining).sum();
        byte[] ba = new byte[size];

        int offset = 0;
        for(ByteBuffer buffer: responseData){
            int remaining = buffer.remaining();
            buffer.get(ba, offset, remaining);
            offset += remaining;
        }

        String s = new String(ba);

        // Transform string into Patient with gson
        Gson g = new Gson();
        Patient patient = g.fromJson(s, Patient.class);

        patientCF.complete(patient);

    }
}
