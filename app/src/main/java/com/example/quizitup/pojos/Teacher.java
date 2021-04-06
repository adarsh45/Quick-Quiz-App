package com.example.quizitup.pojos;

public class Teacher {
    private String uid, name, email, subject;

    public Teacher(){}

    public Teacher(String uid, String name, String email, String subject) {
        this.uid = uid;
        this.name = name;
        this.email = email;
        this.subject = subject;
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

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }
}
