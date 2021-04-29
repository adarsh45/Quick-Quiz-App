package com.adarsh45.quizitup.pojos;

import com.google.firebase.database.IgnoreExtraProperties;

import java.util.Map;

@IgnoreExtraProperties
public class Class {

    private String inviteCode, classTitle, createdBy, teacherName;
    private Map<String, Quiz> quizzesMap;
    private Map<String, Boolean> studentsEnrolledMap;

    public Class(){}

    public Class(String inviteCode, String classTitle, String createdBy, String teacherName) {
        this.inviteCode = inviteCode;
        this.classTitle = classTitle;
        this.createdBy = createdBy;
        this.teacherName = teacherName;
    }

    public Class(String inviteCode, String classTitle, String createdBy, String teacherName, Map<String, Quiz> quizzesMap, Map<String, Boolean> studentsEnrolledMap) {
        this.inviteCode = inviteCode;
        this.classTitle = classTitle;
        this.createdBy = createdBy;
        this.teacherName = teacherName;
        this.quizzesMap = quizzesMap;
        this.studentsEnrolledMap = studentsEnrolledMap;
    }

    public String getInviteCode() {
        return inviteCode;
    }

    public void setInviteCode(String inviteCode) {
        this.inviteCode = inviteCode;
    }

    public String getClassTitle() {
        return classTitle;
    }

    public void setClassTitle(String classTitle) {
        this.classTitle = classTitle;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getTeacherName() {
        return teacherName;
    }

    public void setTeacherName(String teacherName) {
        this.teacherName = teacherName;
    }

    public Map<String, Quiz> getQuizzesMap() {
        return quizzesMap;
    }

    public void setQuizzesMap(Map<String, Quiz> quizzesMap) {
        this.quizzesMap = quizzesMap;
    }

    public Map<String, Boolean> getStudentsEnrolledMap() {
        return studentsEnrolledMap;
    }

    public void setStudentsEnrolledMap(Map<String, Boolean> studentsEnrolledMap) {
        this.studentsEnrolledMap = studentsEnrolledMap;
    }

    public static class Quiz {
        private String quizId, quizTitle, status, duration;
        private Map<String, Question> questionMap;

        public Quiz(){}

        public Quiz(String quizId, String quizTitle, String status, String duration, Map<String, Question> questionMap) {
            this.quizId = quizId;
            this.quizTitle = quizTitle;
            this.status = status;
            this.duration = duration;
            this.questionMap = questionMap;
        }

        public String getQuizId() {
            return quizId;
        }

        public void setQuizId(String quizId) {
            this.quizId = quizId;
        }

        public String getQuizTitle() {
            return quizTitle;
        }

        public void setQuizTitle(String quizTitle) {
            this.quizTitle = quizTitle;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getDuration() {
            return duration;
        }

        public void setDuration(String duration) {
            this.duration = duration;
        }

        public Map<String, Question> getQuestionMap() {
            return questionMap;
        }

        public void setQuestionMap(Map<String, Question> questionMap) {
            this.questionMap = questionMap;
        }

    }

    public static class Question {
        private String quePosition, queTitle, option1, option2, option3, option4, correctOption, explanation;

        public Question(){}

        public Question(String quePosition, String queTitle, String option1, String option2, String option3, String option4, String correctOption, String explanation) {
            this.quePosition = quePosition;
            this.queTitle = queTitle;
            this.option1 = option1;
            this.option2 = option2;
            this.option3 = option3;
            this.option4 = option4;
            this.correctOption = correctOption;
            this.explanation = explanation;
        }

        public String getQuePosition() {
            return quePosition;
        }

        public void setQuePosition(String quePosition) {
            this.quePosition = quePosition;
        }

        public String getQueTitle() {
            return queTitle;
        }

        public void setQueTitle(String queTitle) {
            this.queTitle = queTitle;
        }

        public String getOption1() {
            return option1;
        }

        public void setOption1(String option1) {
            this.option1 = option1;
        }

        public String getOption2() {
            return option2;
        }

        public void setOption2(String option2) {
            this.option2 = option2;
        }

        public String getOption3() {
            return option3;
        }

        public void setOption3(String option3) {
            this.option3 = option3;
        }

        public String getOption4() {
            return option4;
        }

        public void setOption4(String option4) {
            this.option4 = option4;
        }

        public String getCorrectOption() {
            return correctOption;
        }

        public void setCorrectOption(String correctOption) {
            this.correctOption = correctOption;
        }

        public String getExplanation() {
            return explanation;
        }

        public void setExplanation(String explanation) {
            this.explanation = explanation;
        }
    }

}
