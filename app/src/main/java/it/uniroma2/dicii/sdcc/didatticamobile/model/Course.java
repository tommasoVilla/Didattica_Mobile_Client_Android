package it.uniroma2.dicii.sdcc.didatticamobile.model;

import android.os.Bundle;

import java.util.ArrayList;

/*
 * An Course object represents a course inserted in the service of course management.
 * */
public class Course {
    private String id;
    private String name;
    private String department;
    private String year;
    private Integer semester;
    private ArrayList<ScheduleItem> schedule;
    private String description;
    private String teacher;

    public Course(){}

    public Course (Bundle courseBundle){

        ScheduleItem slot1 = new ScheduleItem((String)courseBundle.get("day1"),
                (String)courseBundle.get("startTime1"), (String)courseBundle.get("endTime1"),
                (String)courseBundle.get("room1"));
        ScheduleItem slot2 = new ScheduleItem((String)courseBundle.get("day2"),
                (String)courseBundle.get("startTime2"), (String)courseBundle.get("endTime2"),
                (String)courseBundle.get("room2"));
        ScheduleItem slot3 = new ScheduleItem((String)courseBundle.get("day3"),
                (String)courseBundle.get("startTime3"), (String)courseBundle.get("endTime3"),
                (String)courseBundle.get("room3"));
        this.schedule = new ArrayList<>();
        this.schedule.add(slot1);
        this.schedule.add(slot2);
        this.schedule.add(slot3);

        this.name = (String) courseBundle.get("name");
        this.department = (String) courseBundle.get("department");
        this.year = (String) courseBundle.get("year");
        this.semester = (Integer) courseBundle.get("semester");
        this.description = (String) courseBundle.get("description");
        this.teacher = (String) courseBundle.get("teacher");
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDepartment() {
        return department;
    }

    public String getYear() {
        return year;
    }

    public Integer getSemester() {
        return semester;
    }

    public ArrayList<ScheduleItem> getSchedule() {
        return schedule;
    }

    public String getDescription() {
        return description;
    }

    public String getTeacher() {
        return teacher;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public void setSemester(Integer semester) {
        this.semester = semester;
    }

    public void setSchedule(ArrayList<ScheduleItem> schedule) {
        this.schedule = schedule;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setTeacher(String teacher) {
        this.teacher = teacher;
    }
}
