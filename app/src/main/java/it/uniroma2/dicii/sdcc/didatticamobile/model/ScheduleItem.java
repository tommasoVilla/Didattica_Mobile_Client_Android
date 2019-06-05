package it.uniroma2.dicii.sdcc.didatticamobile.model;

import org.json.JSONException;
import org.json.JSONObject;

/*
 * An ScheduleItem object represents the information for each lesson of a course
 * inserted in the service of course management.
 * */
public class ScheduleItem {
    private String day;
    private String startTime;
    private String endTime;
    private String room;

    public ScheduleItem(){}

    public ScheduleItem (String day, String startTime, String endTime, String room){
        this.day = day;
        this.startTime = startTime;
        this.endTime = endTime;
        this.room = room;
    }

    public JSONObject toJsonObject() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("day", this.getDay());
            jsonObject.put("startTime", this.getStartTime());
            jsonObject.put("endTime", this.getEndTime());
            jsonObject.put("room", this.getRoom());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    public String getDay() {
        return day;
    }

    public String getStartTime() {
        return startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public String getRoom() {
        return room;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public void setRoom(String room) {
        this.room = room;
    }
}
