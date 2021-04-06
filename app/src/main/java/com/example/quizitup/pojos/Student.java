package com.example.quizitup.pojos;

public class Student {

    private String uid, name, email, roll;

    public Student(){}

    public Student(String uid, String name, String email, String roll) {
        this.uid = uid;
        this.name = name;
        this.email = email;
        this.roll = roll;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRoll() {
        return roll;
    }

    public void setRoll(String roll) {
        this.roll = roll;
    }
}
