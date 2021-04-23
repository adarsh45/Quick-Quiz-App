package com.example.quizitup.pojos;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.database.IgnoreExtraProperties;

import java.util.HashMap;

@IgnoreExtraProperties
public class Class implements Parcelable {

    private String inviteCode, classTitle, createdBy, teacherName;
    private HashMap<String, Quiz> quizzesMap;
    private HashMap<String, String> studentsEnrolledMap;

    public Class(){}

    public Class(String inviteCode, String classTitle, String createdBy, String teacherName) {
        this.inviteCode = inviteCode;
        this.classTitle = classTitle;
        this.createdBy = createdBy;
        this.teacherName = teacherName;
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

    public HashMap<String, Quiz> getQuizzesMap() {
        return quizzesMap;
    }

    public void setQuizzesMap(HashMap<String, Quiz> quizzesMap) {
        this.quizzesMap = quizzesMap;
    }

    public HashMap<String, String> getStudentsEnrolledMap() {
        return studentsEnrolledMap;
    }

    public void setStudentsEnrolledMap(HashMap<String, String> studentsEnrolledMap) {
        this.studentsEnrolledMap = studentsEnrolledMap;
    }

    public static class Quiz{
        private String quizId, quizTitle, status, duration;
        private HashMap<String, Question> questionMap;

        public Quiz(){}

        public Quiz(String quizId, String quizTitle, String status, String duration, HashMap<String, Question> questionMap) {
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

        public HashMap<String, Question> getQuestionMap() {
            return questionMap;
        }

        public void setQuestionMap(HashMap<String, Question> questionMap) {
            this.questionMap = questionMap;
        }


    }

    public static class Question {
        private String quePosition, queTitle, option1, option2, option3, option4, correctOption;

        public Question(){}

        public Question(String quePosition, String queTitle, String option1, String option2, String option3, String option4, String correctOption) {
            this.quePosition = quePosition;
            this.queTitle = queTitle;
            this.option1 = option1;
            this.option2 = option2;
            this.option3 = option3;
            this.option4 = option4;
            this.correctOption = correctOption;
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

    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.inviteCode);
        dest.writeString(this.classTitle);
        dest.writeString(this.createdBy);
        dest.writeString(this.teacherName);
    }

    public void readFromParcel(Parcel source) {
        this.inviteCode = source.readString();
        this.classTitle = source.readString();
        this.createdBy = source.readString();
        this.teacherName = source.readString();
    }

    protected Class(Parcel in) {
        this.inviteCode = in.readString();
        this.classTitle = in.readString();
        this.createdBy = in.readString();
        this.teacherName = in.readString();
    }

    public static final Creator<Class> CREATOR = new Creator<Class>() {
        @Override
        public Class createFromParcel(Parcel source) {
            return new Class(source);
        }

        @Override
        public Class[] newArray(int size) {
            return new Class[size];
        }
    };
}
