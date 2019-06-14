package it.uniroma2.dicii.sdcc.didatticamobile.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AndroidException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import it.uniroma2.dicii.sdcc.didatticamobile.R;
import it.uniroma2.dicii.sdcc.didatticamobile.model.Exam;

/* This is the Adapter Class used to show a List of Exam objects inside a ListView*/
public class ExamsListAdapter extends ArrayAdapter {

    private int listItemId; // Identifier of the XML describing ListView item layout
    private LayoutInflater inflater; // Used to build a ListView item from the XML
    private List<Exam> exams; // Exams that will populate the ListView

    public ExamsListAdapter(@NonNull Context context, int listItemId, @NonNull List<Exam> exams) {
        super(context, listItemId, exams);
        this.listItemId = listItemId;
        this.inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.exams = exams;
    }

    // This method is automatically invoked to populated the courseIndex-th item of the ListView
    @NonNull
    @Override
    public View getView(int examIndex, @Nullable View listItemView, @NonNull ViewGroup parent) {

        // The View object corresponding to the list item is built if necessary
        if (listItemView == null) {
            listItemView = inflater.inflate(listItemId, null);
        }

        // The list item is populated with information about the exam
        fillListItem(listItemView, examIndex);

        return listItemView;

    }

    private void fillListItem(View listItemView, int examIndex) {

        TextView tvCall = listItemView.findViewById(R.id.tvExamItemCall);
        TextView tvSchedule = listItemView.findViewById(R.id.tvExamItemSchedule);
        TextView tvExpiration = listItemView.findViewById(R.id.tvExamItemExp);
        Exam exam = exams.get(examIndex);
        tvCall.setText(exam.getCall() + "° Appello");
        tvSchedule.setText(exam.getDate() + " " + exam.getStartTime() + " Aula: " + exam.getRoom());
        tvExpiration.setText("Scadenza prenotazioni: " + exam.getExpirationDate());
    }
}