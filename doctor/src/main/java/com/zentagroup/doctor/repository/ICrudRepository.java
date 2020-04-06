package com.zentagroup.doctor.repository;

public interface ICrudRepository<T> {
    public T create();
    public T retrieve();
    public T update();
    public T delete();
}
