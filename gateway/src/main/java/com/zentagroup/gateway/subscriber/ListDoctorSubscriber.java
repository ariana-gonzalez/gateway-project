package com.zentagroup.gateway.subscriber;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zentagroup.gateway.dto.Doctor;

import java.net.http.HttpResponse;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.Flow;

public class ListDoctorSubscriber implements HttpResponse.BodySubscriber<List<Doctor>> {
    final CompletableFuture<List< Doctor>> listCF = new CompletableFuture<List<Doctor>>();
    Flow.Subscription subscription;
    List<ByteBuffer> responseData = new ArrayList<>();

    @Override
    public CompletionStage<List<Doctor>> getBody() {
        return listCF;
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
        listCF.completeExceptionally(throwable);
    }

    @Override
    public void onComplete() {
        int size = responseData.stream().mapToInt(ByteBuffer::remaining).sum();
        byte[] ba = new byte[size];

        int offset = 0;
        for (ByteBuffer buffer : responseData) {
            int remaining = buffer.remaining();
            buffer.get(ba, offset, remaining);
            offset += remaining;
        }

        String s = new String(ba);

        List<Doctor> list = new Gson().fromJson(s, new TypeToken<ArrayList<Doctor>>() {
        }.getType());

        listCF.complete(list);
    }
}
