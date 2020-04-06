package com.zentagroup.gateway.dto;

import java.util.List;

public class EntityList<T1, T2> {

    private T1 entity;
    private List<T2> list;

    public EntityList() {
    }

    public T1 getEntity() {
        return entity;
    }

    public void setEntity(T1 entity) {
        this.entity = entity;
    }

    public List<T2> getList() {
        return list;
    }

    public void setList(List<T2> list) {
        this.list = list;
    }

    @Override
    public String toString() {
        return "EntityList{" +
                "entity=" + entity +
                ", list=" + list +
                '}';
    }
}
