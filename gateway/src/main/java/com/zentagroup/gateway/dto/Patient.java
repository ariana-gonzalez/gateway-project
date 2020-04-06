package com.zentagroup.gateway.dto;

public class Patient {
    private int id;
    private String name;
    private String phone;
    private String address;

    public Patient() {
    }

    public Patient(String name, String phoneNumber, String address) {
        this.name = name;
        this.phone = phoneNumber;
        this.address = address;
    }

    public Patient(int id, String name, String phoneNumber, String address) {
        this.id = id;
        this.name = name;
        this.phone = phoneNumber;
        this.address = address;
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

    public String getPhoneNumber() {
        return phone;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phone = phoneNumber;
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
