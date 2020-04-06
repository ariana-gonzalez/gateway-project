package com.zentagroup.patient.model;

public class Patient {
    public int id;
    public String name;
    public String phone;
    public String address;

    public Patient() {
    }

    public Patient(String name, String phone, String address) {
        this.name = name;
        this.phone = phone;
        this.address = address;
    }

    public Patient(int id, String name, String phone, String address) {
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.address = address;
    }

    public Patient(Patient patient){
        this(patient.getId(), patient.getName(), patient.getPhone(), patient.getAddress());
    }

    public Patient(int id, Patient patient){
        this(id, patient.getName(), patient.getPhone(), patient.getAddress());
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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public String toString() {
        return "Patient{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", phoneNumber='" + phone + '\'' +
                ", address='" + address + '\'' +
                '}';
    }


}
