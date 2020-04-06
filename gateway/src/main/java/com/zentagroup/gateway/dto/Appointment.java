package com.zentagroup.gateway.dto;

public class Appointment {
    Doctor doctor;
    Patient patient;
    String date;

    public Appointment() {
    }

    public Doctor getDoctor() {
        return doctor;
    }

    public void setDoctor(Doctor doctor) {
        this.doctor = doctor;
    }

    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "Appointment{" +
                "doctor=" + doctor +
                ", patient=" + patient +
                ", date='" + date + '\'' +
                '}';
    }
}
