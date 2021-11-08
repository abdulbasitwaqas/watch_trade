package com.watchtrading.watchtrade.Models;

public class User {

    String fullName;
    String email;
    String phoneNumber;
    String address;

    public User(){};
    public User(String fullName, String email, String phoneNumber, String address){
        this.fullName =fullName;
        this.email=email;
        this.phoneNumber=phoneNumber;
        this.address=address;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }



    public String getEmail() {
        return email;
    }


}