package ru.anrad.moonday;

import android.app.Activity;
//import android.support.v4.app.Fragment;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
//import android.support.v4.app.FragmentManager;
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

import ru.anrad.moonday.dao.CurrentDayDataSource;
import ru.anrad.moonday.dao.HistoryDataSource;
import ru.anrad.moonday.dao.MoonDayStatistic;
import ru.anrad.moonday.dao.Status;
import ru.anrad.moonday.dao.StatusService;

/**
 * A placeholder fragment containing a simple view.
 */

public class BeginActiveDayFragment extends Fragment {

    private static final String DATE_FORMAT_STRING = "EEE d MMMM";
    private static SimpleDateFormat DF = new SimpleDateFormat(DATE_FORMAT_STRING);

    public static final int DATE_PICKER_DIALOG_REQUEST_CODE = 100;

    OnFragmentInteractionListener interactionListener;
    DatePickerDialog dialog;

    //CurrentDayDataSource currentDS;
    //HistoryDataSource historyDS;

    StatusService statusService;
    Status status;

    //Date end;
    //Date beginForecast;
    //long daysLeft;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_begin_active_day, container, false);
        Button b = (Button) view.findViewById(R.id.fragment_begin_active_day_action);
        b.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onButtonActionClick(v);
            }
        });

        TextView tBegin = (TextView) view.findViewById(R.id.fragment_begin_active_day_from);
        if (status.getBegin() != null) {
            tBegin.setText(DF.format(status.getBegin()));
        } else {
            tBegin.setText("Нет данных");
            //Snackbar.make(view, "Нет предыдущих данных", Snackbar.LENGTH_LONG).setAction("Action", null).show();

        }

        TextView tEnd = (TextView) view.findViewById(R.id.fragment_begin_active_day_to);
        TextView tDaysLeft = (TextView) view.findViewById(R.id.fragment_begin_active_day_left);
        TextView tDaysLeftCaption = (TextView) view.findViewById(R.id.fragment_begin_active_day_left_caption);

        if (status.getForecast() != null) {
            tEnd.setText(DF.format(status.getForecast()));
            if (status.getForecastLeftDays() < 0) {
                tDaysLeftCaption.setText("Задержка:");
                tDaysLeft.setText((status.getForecastLeftDays() * (-1)) + " дней");
            } else {
                tDaysLeftCaption.setText("Осталось:");
                tDaysLeft.setText(status.getForecastLeftDays() + " дней");
            }
        } else {
            tEnd.setText("Недостаточно данных");
            tDaysLeft.setText("Недостаточно данных");
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

        //currentDS = CurrentDayDataSource.getInstance(context.getApplicationContext());
        //historyDS = HistoryDataSource.getInstance(context.getApplicationContext());
        statusService = new StatusService(context);
        //
        status = statusService.getCurrentStatus();

        //end = null;
        //beginForecast = null;
        //daysLeft = 0;
        //if (currentDS.getEnd() != null) {
        //    end = currentDS.getEnd();
        //   MoonDayStatistic stat= historyDS.getStatistic();
        //    if ( stat != null && stat.hasRest() ) {
        //        beginForecast = stat.getBeginForecast(end);
        //        daysLeft = stat.getBeginForecastLeftDays(end);
        //    }
        //}
    }

    public void onButtonActionClick(View view) {
        dialog.setTargetFragment(this, DATE_PICKER_DIALOG_REQUEST_CODE);
        dialog.show(this.getFragmentManager(), "DatePickerDialog");
        //this.getFragmentManager().beginTransaction().replace(R.id.activity_main_fragment,dialog);

    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Make sure fragment codes match up
        if (requestCode == DATE_PICKER_DIALOG_REQUEST_CODE & resultCode == DatePickerDialog.RESULT_OK) {
            String dateString = data.getStringExtra("DATE");
            //Log.v(this.getClass().getName(), "Begin Active Day: " + dateString);
            SimpleDateFormat df = new SimpleDateFormat(DatePickerDialog.CalendarDateFormat.DATE_FORMAT_STRING);
            try {
                Date d = df.parse(dateString);
                try {
                    //currentDS.beginDay(d);
                    statusService.changeStatus(d);
                    interactionListener.onFragmentInteraction(OnFragmentInteractionListener.DAY_HAS_BEGAN);
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
        inflater.inflate(R.menu.menu_begin_active_day_fragment, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.menu_begin_active_day_fragment_undo) {
            //currentDS.undoBeginDay();
            statusService.undoCurrentStatus();
            interactionListener.onFragmentInteraction(OnFragmentInteractionListener.DAY_HAS_BEGAN);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
