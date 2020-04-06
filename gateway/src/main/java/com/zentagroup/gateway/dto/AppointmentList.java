package com.zentagroup.gateway.dto;

import java.util.Map;

public class AppointmentList<T1, T2> {
    T1 mainEntity;
    Map<String, T2> appointments;

    public AppointmentList() {
    }

    public T1 getMainEntity() {
        return mainEntity;
    }

    public void setMainEntity(T1 mainEntity) {
        this.mainEntity = mainEntity;
    }

    public Map<String, T2> getAppointments() {
        return appointments;
    }

    public void setAppointments(Map<String, T2> appointments) {
        this.appointments = appointments;
    }

    @Override
    public String toString() {
        return "AppointmentList{" +
                "mainEntity=" + mainEntity +
                ", appointments=" + appointments +
                '}';
    }
}
