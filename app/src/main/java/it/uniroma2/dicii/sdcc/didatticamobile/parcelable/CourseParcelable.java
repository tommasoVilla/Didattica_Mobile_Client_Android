package it.uniroma2.dicii.sdcc.didatticamobile.parcelable;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

import it.uniroma2.dicii.sdcc.didatticamobile.model.Course;
import it.uniroma2.dicii.sdcc.didatticamobile.model.ScheduleItem;

/* A CourseParcelable object is a kind of Course that can be passed through activities.
* Indeed, Android requires that an object passed with an Intent implement Parcelable
* interface.*/
public class CourseParcelable implements Parcelable {

    private String id;
    private String name;
    private String department;
    private String year;
    private Integer semester;
    private String description;
    private String teacher;
    private String schedule1;
    private String schedule2;
    private String schedule3;

    public CourseParcelable(Course course) {
        id = course.getId();
        name = course.getName();
        department = course.getDepartment();
        year = course.getYear();
        semester = course.getSemester();
        description = course.getDescription();
        teacher = course.getTeacher();
        ArrayList<ScheduleItem> schedule = course.getSchedule();
        schedule1 = schedule.get(0).getDay() + " " + schedule.get(0).getStartTime() + "-" + schedule.get(0).getEndTime() + " " + schedule.get(0).getRoom();
        schedule2 = schedule.get(1).getDay() + " " + schedule.get(1).getStartTime() + "-" + schedule.get(1).getEndTime() + " " + schedule.get(1).getRoom();
        schedule3 = schedule.get(2).getDay() + " " + schedule.get(2).getStartTime() + "-" + schedule.get(2).getEndTime() + " " + schedule.get(2).getRoom();
    }

    private CourseParcelable(Parcel in) {
        id = in.readString();
        name = in.readString();
        department = in.readString();
        year = in.readString();
        semester = in.readInt();
        description = in.readString();
        teacher = in.readString();
        schedule1 = in.readString();
        schedule2 = in.readString();
        schedule3 = in.readString();
    }

    public static final Creator<CourseParcelable> CREATOR = new Creator<CourseParcelable>() {
        @Override
        public CourseParcelable createFromParcel(Parcel in) {
            return new CourseParcelable(in);
        }

        @Override
        public CourseParcelable[] newArray(int size) {
            return new CourseParcelable[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(name);
        dest.writeString(department);
        dest.writeString(year);
        dest.writeInt(semester);
        dest.writeString(description);
        dest.writeString(teacher);
        dest.writeString(schedule1);
        dest.writeString(schedule2);
        dest.writeString(schedule3);
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

    public String getDescription() {
        return description;
    }

    public String getTeacher() {
        return teacher;
    }

    public String getSchedule1() {
        return schedule1;
    }

    public String getSchedule2() {
        return schedule2;
    }

    public String getSchedule3() {
        return schedule3;
    }
}
