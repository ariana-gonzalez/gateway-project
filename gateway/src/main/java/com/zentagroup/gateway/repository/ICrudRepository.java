package com.zentagroup.gateway.repository;

import java.util.concurrent.CompletableFuture;

public interface ICrudRepository<T> {
    public CompletableFuture<T> create(T requestBody);
    public CompletableFuture<T> retrieve(int id);
    public CompletableFuture<T> update(T requestBody);
    public CompletableFuture<T> delete(int id);
}
