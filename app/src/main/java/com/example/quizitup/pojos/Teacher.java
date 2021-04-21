package com.example.quizitup.pojos;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class Teacher {
    private String teacherId, teacherName, email, mobile;

    public Teacher(){}

    public Teacher(String teacherId, String teacherName, String email, String mobile) {
        this.teacherId = teacherId;
        this.teacherName = teacherName;
        this.email = email;
        this.mobile = mobile;
    }

    public String getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(String teacherId) {
        this.teacherId = teacherId;
    }

    public String getTeacherName() {
        return teacherName;
    }

    public void setTeacherName(String teacherName) {
        this.teacherName = teacherName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }
}
