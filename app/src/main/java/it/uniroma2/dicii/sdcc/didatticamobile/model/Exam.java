package it.uniroma2.dicii.sdcc.didatticamobile.model;

import android.os.Bundle;

import java.util.ArrayList;

/*
 * An Exam object represents an exam inserted in the persistence layer.
 * */
public class Exam {
    private String id;
    private String course;
    private int call;
    private String date;
    private String expirationDate;
    private String room;
    private String startTime;
    private ArrayList<String> students;

    public Exam(){}

    public Exam(Bundle examInformation){
        this.course = examInformation.getString("course");
        this.call = examInformation.getInt("call");
        String date = examInformation.getInt("examDay") + "-" +
                examInformation.getInt("examMonth") + "-" + examInformation.getInt("examYear");
        this.date = date;
        String expirationDate = examInformation.getInt("expDay") + "-" +
                examInformation.getInt("expMonth") + "-" + examInformation.getInt("expYear");
        this.expirationDate = expirationDate;
        this.room = examInformation.getString("room");
        this.startTime = examInformation.getString("startTime");
        this.students = null;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCourse() {
        return course;
    }

    public void setCourse(String course) {
        this.course = course;
    }

    public int getCall() {
        return call;
    }

    public void setCall(int call) {
        this.call = call;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(String expirationDate) {
        this.expirationDate = expirationDate;
    }

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public ArrayList<String> getStudents() {
        return students;
    }

    public void setStudents(ArrayList<String> students) {
        this.students = students;
    }
}
