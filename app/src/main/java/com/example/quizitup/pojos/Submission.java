package com.example.quizitup.pojos;

public class Submission {

    private String studentId, studentName, studentMobile, score;

    public Submission(){}

    public Submission(String studentId, String studentName, String studentMobile, String score) {
        this.studentId = studentId;
        this.studentName = studentName;
        this.studentMobile = studentMobile;
        this.score = score;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public String getStudentMobile() {
        return studentMobile;
    }

    public void setStudentMobile(String studentMobile) {
        this.studentMobile = studentMobile;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }
}
