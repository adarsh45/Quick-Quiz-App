package com.example.quizitup.pojos;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.database.IgnoreExtraProperties;

import java.util.HashMap;

@IgnoreExtraProperties
public class Student implements Parcelable {

    private String studentId, studentName, email, mobile;
    private HashMap<String, String> enrolledClasses;
    private HashMap<String, AttemptedQuiz> attemptedQuizzesMap;


    public Student(){}

    public Student(String studentId, String studentName, String email, String mobile) {
        this.studentId = studentId;
        this.studentName = studentName;
        this.email = email;
        this.mobile = mobile;
    }

    public Student(String studentId, String studentName, String mobile, String email, HashMap<String, String> enrolledClasses, HashMap<String, AttemptedQuiz> attemptedQuizzesMap) {
        this.studentId = studentId;
        this.studentName = studentName;
        this.mobile = mobile;
        this.email = email;
        this.enrolledClasses = enrolledClasses;
        this.attemptedQuizzesMap = attemptedQuizzesMap;
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

    public HashMap<String, String> getEnrolledClasses() {
        return enrolledClasses;
    }

    public void setEnrolledClasses(HashMap<String, String> enrolledClasses) {
        this.enrolledClasses = enrolledClasses;
    }

    public HashMap<String, AttemptedQuiz> getAttemptedQuizzesMap() {
        return attemptedQuizzesMap;
    }

    public void setAttemptedQuizzesMap(HashMap<String, AttemptedQuiz> attemptedQuizzesMap) {
        this.attemptedQuizzesMap = attemptedQuizzesMap;
    }

    public class AttemptedQuiz{
        private String quizId, score;
        private HashMap<String, AnsweredQuestion> questionsMap;

        public AttemptedQuiz(String quizId, String score, HashMap<String, AnsweredQuestion> questionsMap) {
            this.quizId = quizId;
            this.score = score;
            this.questionsMap = questionsMap;
        }

        public String getQuizId() {
            return quizId;
        }

        public void setQuizId(String quizId) {
            this.quizId = quizId;
        }

        public String getScore() {
            return score;
        }

        public void setScore(String score) {
            this.score = score;
        }

        public HashMap<String, AnsweredQuestion> getQuestionsMap() {
            return questionsMap;
        }

        public void setQuestionsMap(HashMap<String, AnsweredQuestion> questionsMap) {
            this.questionsMap = questionsMap;
        }
    }

    public class AnsweredQuestion{
        private String quePosition, selectedOption, correctOption;

        public AnsweredQuestion(){}

        public AnsweredQuestion(String quePosition, String selectedOption, String correctOption) {
            this.quePosition = quePosition;
            this.selectedOption = selectedOption;
            this.correctOption = correctOption;
        }

        public String getQuePosition() {
            return quePosition;
        }

        public void setQuePosition(String quePosition) {
            this.quePosition = quePosition;
        }

        public String getSelectedOption() {
            return selectedOption;
        }

        public void setSelectedOption(String selectedOption) {
            this.selectedOption = selectedOption;
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
        dest.writeString(this.studentId);
        dest.writeString(this.studentName);
        dest.writeString(this.email);
        dest.writeString(this.mobile);
        dest.writeSerializable(this.enrolledClasses);
        dest.writeSerializable(this.attemptedQuizzesMap);
    }

    public void readFromParcel(Parcel source) {
        this.studentId = source.readString();
        this.studentName = source.readString();
        this.email = source.readString();
        this.mobile = source.readString();
        this.enrolledClasses = (HashMap<String, String>) source.readSerializable();
        this.attemptedQuizzesMap = (HashMap<String, AttemptedQuiz>) source.readSerializable();
    }

    protected Student(Parcel in) {
        this.studentId = in.readString();
        this.studentName = in.readString();
        this.email = in.readString();
        this.mobile = in.readString();
        this.enrolledClasses = (HashMap<String, String>) in.readSerializable();
        this.attemptedQuizzesMap = (HashMap<String, AttemptedQuiz>) in.readSerializable();
    }

    public static final Parcelable.Creator<Student> CREATOR = new Parcelable.Creator<Student>() {
        @Override
        public Student createFromParcel(Parcel source) {
            return new Student(source);
        }

        @Override
        public Student[] newArray(int size) {
            return new Student[size];
        }
    };
}
