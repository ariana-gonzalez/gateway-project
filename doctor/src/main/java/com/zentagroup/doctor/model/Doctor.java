package com.zentagroup.doctor.model;

public class Doctor {
    private int id;
    private String name;
    private String specialization;

    public Doctor() {
    }

    public Doctor(String name, String specialization) {
        this.name = name;
        this.specialization = specialization;
    }

    public Doctor(int id, String name, String specialization) {
        this.id = id;
        this.name = name;
        this.specialization = specialization;
    }

    public Doctor(Doctor doctor){
        this(doctor.getId(), doctor.getName(), doctor.getSpecialization());
    }

    public Doctor(int id, Doctor doctor){
        this(id, doctor.getName(), doctor.getSpecialization());
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSpecialization() {
        return specialization;
    }

    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }
}
