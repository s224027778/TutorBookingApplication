package com.example.cc;

public class TutorProfile {
    private String name;
    private String firstName;
    private String lastName;
    private String phoneNumber;

    public TutorProfile(String name, String firstName, String lastName, String phoneNumber) {
        this.name = name;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    //public TutorProfile(String tutorName, String firstName, String lastName, String phoneNumber) {
    //}
}
