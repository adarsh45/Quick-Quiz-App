package com.example.quizitup.pojos;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.database.IgnoreExtraProperties;

import java.util.HashMap;

@IgnoreExtraProperties
public class Teacher implements Parcelable {
    private String teacherId, teacherName, email, mobile;
    private HashMap<String, ClassCreated> classCreatedMap;

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

    public HashMap<String, ClassCreated> getClassCreatedMap() {
        return classCreatedMap;
    }

    public void setClassCreatedMap(HashMap<String, ClassCreated> classCreatedMap) {
        this.classCreatedMap = classCreatedMap;
    }

    public static class ClassCreated implements Parcelable {
        private String inviteCode;
        private HashMap<String, String> studentsEnrolledMap;

        public ClassCreated(){}

        public ClassCreated(String inviteCode) {
            this.inviteCode = inviteCode;
        }

        public ClassCreated(String inviteCode, HashMap<String, String> studentsEnrolledMap) {
            this.inviteCode = inviteCode;
            this.studentsEnrolledMap = studentsEnrolledMap;
        }

        public String getInviteCode() {
            return inviteCode;
        }

        public void setInviteCode(String inviteCode) {
            this.inviteCode = inviteCode;
        }

        public HashMap<String, String> getStudentsEnrolledMap() {
            return studentsEnrolledMap;
        }

        public void setStudentsEnrolledMap(HashMap<String, String> studentsEnrolledMap) {
            this.studentsEnrolledMap = studentsEnrolledMap;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.inviteCode);
            dest.writeSerializable(this.studentsEnrolledMap);
        }

        public void readFromParcel(Parcel source) {
            this.inviteCode = source.readString();
            this.studentsEnrolledMap = (HashMap<String, String>) source.readSerializable();
        }

        protected ClassCreated(Parcel in) {
            this.inviteCode = in.readString();
            this.studentsEnrolledMap = (HashMap<String, String>) in.readSerializable();
        }

        public static final Creator<ClassCreated> CREATOR = new Creator<ClassCreated>() {
            @Override
            public ClassCreated createFromParcel(Parcel source) {
                return new ClassCreated(source);
            }

            @Override
            public ClassCreated[] newArray(int size) {
                return new ClassCreated[size];
            }
        };
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.teacherId);
        dest.writeString(this.teacherName);
        dest.writeString(this.email);
        dest.writeString(this.mobile);
    }

    public void readFromParcel(Parcel source) {
        this.teacherId = source.readString();
        this.teacherName = source.readString();
        this.email = source.readString();
        this.mobile = source.readString();
    }

    protected Teacher(Parcel in) {
        this.teacherId = in.readString();
        this.teacherName = in.readString();
        this.email = in.readString();
        this.mobile = in.readString();
    }

    public static final Creator<Teacher> CREATOR = new Creator<Teacher>() {
        @Override
        public Teacher createFromParcel(Parcel source) {
            return new Teacher(source);
        }

        @Override
        public Teacher[] newArray(int size) {
            return new Teacher[size];
        }
    };
}
