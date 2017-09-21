package ru.anrad.moonday;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.app.Fragment;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import ru.anrad.moonday.dao.CurrentDayDataSource;
import ru.anrad.moonday.dao.HistoryDataSource;
import ru.anrad.moonday.dao.MoonDayStatistic;

/**
 * A placeholder fragment containing a simple view.
 */
public class FinishActiveDayFragment extends Fragment {

    private static final String DATE_FORMAT_STRING = "EEE d MMMM";
    private static SimpleDateFormat DF = new SimpleDateFormat(DATE_FORMAT_STRING, Locale.getDefault());
    public static final int DATE_PICKER_DIALOG_REQUEST_CODE = 100;

    OnFragmentInteractionListener interactionListener;
    DatePickerDialog dialog;
    CurrentDayDataSource currentDS;
    HistoryDataSource historyDS;

    Date begin;
    Date endForecast;
    long daysLeft;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_finish_active_day, container, false);
        Button b = (Button) view.findViewById(R.id.fragment_finish_active_day_action);
        b.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onButtonActionClick(v);
            }
        });

        TextView tBegin = (TextView) view.findViewById(R.id.fragment_finish_active_day_from);
        tBegin.setText(DF.format(begin));

        TextView tEnd = (TextView) view.findViewById(R.id.fragment_finish_active_day_to);
        TextView tDaysLeft = (TextView) view.findViewById(R.id.fragment_finish_active_day_left);
        if (endForecast != null) {
            tEnd.setText(DF.format(endForecast));
            tDaysLeft.setText(daysLeft + " дней");
        } else {
            tEnd.setText("???");
        }

        dialog = new DatePickerDialog();

        return view;
    }

    @Override
    public void onAttach (Activity context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            interactionListener = (OnFragmentInteractionListener) context;
            //Log.v(this.getClass().getName(), "onAttach");
            //Snackbar.make(this.getView(), context.toString()+":onAttach", Snackbar.LENGTH_LONG).setAction("Action", null).show();
        } else {
            throw new RuntimeException(context.toString() + " must implement OnFragmentInteractionListener");
        }

        currentDS = CurrentDayDataSource.getInstance(context.getApplicationContext());
        historyDS = HistoryDataSource.getInstance(context.getApplicationContext());
        //
        begin = currentDS.getBegin();
        endForecast = null;
        daysLeft = 0;
        //
        MoonDayStatistic stat= historyDS.getStatistic();
        if ( stat != null && stat.hasHot() ) {
            endForecast = stat.getEndForecast(begin);
            daysLeft = stat.getEndForecastLeftDays(begin);
        }

    }

    public void onButtonActionClick(View view) {
        //Log.v(this.getClass().getName(), "Button Begin has pressed");
        dialog.setTargetFragment(this, DATE_PICKER_DIALOG_REQUEST_CODE);
        dialog.show(this.getFragmentManager(), "DatePickerDialog");
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Make sure fragment codes match up
        if (requestCode == DATE_PICKER_DIALOG_REQUEST_CODE & resultCode == DatePickerDialog.RESULT_OK) {
            String dateString = data.getStringExtra("DATE");
            //Log.v(this.getClass().getName(), "End Active Day: " + dateString);
            try {
                Date end = new SimpleDateFormat(DatePickerDialog.CalendarDateFormat.DATE_FORMAT_STRING, Locale.getDefault()).parse(dateString);
                Date begin = currentDS.getBegin();
                try {
                    currentDS.finishDay(end);
                    historyDS.putItem(begin, end);
                    interactionListener.onFragmentInteraction(OnFragmentInteractionListener.DAY_HAS_ENDED);
                } catch (IllegalArgumentException e) {
                    Snackbar.make(this.getView(), e.getMessage(), Snackbar.LENGTH_LONG).setAction("Action", null).show();
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_finish_active_day_fragment, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.menu_finish_active_day_fragment_undo) {
            currentDS.undoBeginDay();
            interactionListener.onFragmentInteraction(OnFragmentInteractionListener.DAY_HAS_ENDED);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
