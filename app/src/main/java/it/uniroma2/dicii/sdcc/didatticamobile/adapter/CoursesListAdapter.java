package it.uniroma2.dicii.sdcc.didatticamobile.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import it.uniroma2.dicii.sdcc.didatticamobile.R;
import it.uniroma2.dicii.sdcc.didatticamobile.model.Course;

/* This is the Adapter Class used to show a List of Course objects inside a ListView*/
public class CoursesListAdapter extends ArrayAdapter {

    private int listItemId; // Identifier of the XML describing ListView item layout
    private LayoutInflater inflater; // Used to build a ListView item from the XML
    private List<Course> courses; // Courses that will populate the ListView

    public CoursesListAdapter(@NonNull Context context, int listItemId, @NonNull List<Course> courses) {
        super(context, listItemId, courses);
        this.listItemId = listItemId;
        this.inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.courses = courses;
    }

    // This method is automatically invoked to populated the courseIndex-th item of the ListView
    @NonNull
    @Override
    public View getView(int courseIndex, @Nullable View listItemView, @NonNull ViewGroup parent) {

        // The View object corresponding to the list item is built if necessary
        if (listItemView == null) {
            listItemView = inflater.inflate(listItemId, null);
        }

        // The list item is populated with name and teacher of the course
        fillListItem(listItemView, courseIndex);

        return listItemView;

    }

    private void fillListItem(View listItemView, int courseIndex) {
        TextView tvName = listItemView.findViewById(R.id.tvCourseItemName);
        TextView tvTeacher = listItemView.findViewById(R.id.tvCourseItemTeacher);
        TextView tvCourseItemYear = listItemView.findViewById(R.id.tvCourseItemYear);
        tvName.setText(courses.get(courseIndex).getName());
        tvTeacher.setText(courses.get(courseIndex).getTeacher());
        tvCourseItemYear.setText(courses.get(courseIndex).getYear());
    }
}
