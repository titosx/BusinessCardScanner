package com.example.businesscardscanner;

public class CardItem {
    private String name;
    private String phone;
    private String address;
    private String city;
    private String email;

    public CardItem(String name, String phone, String address, String city, String email) {
        this.name = name;
        this.phone = phone;
        this.address = address;
        this.city = city;
        this.email = email;
    }

    public String getName() {
        return this.name;
    }

    public String getPhone() {
        return this.phone;
    }

    public String getAddress() {
        return this.address;
    }

    public String getCity() {
        return this.city;
    }

    public String getEmail() {
        return this.email;
    }
}
