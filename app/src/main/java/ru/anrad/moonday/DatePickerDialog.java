package ru.anrad.moonday;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DatePickerDialog extends DialogFragment {

    public static final int RESULT_OK = 0;
    public static final int RESULT_CANCEL = 1;

    //DatePickerDialogListener dateListener;
    public interface DatePickerDialogListener {
        void onSetDate(Date d);
        Date onGetDate();
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        /*
        try {
            // Instantiate the NoticeDialogListener so we can send events to the host
            dateListener = (DatePickerDialogListener) activity;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(activity.toString() + " must implement DatePickerDialogListener");
        } */

    }



    //private TextView tvDay;
    private DatePicker dpDatepicker;
    private Calendar calendar;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        View v = inflater.inflate(R.layout.dialog_datepicker, container);

        dpDatepicker = (DatePicker) v.findViewById(R.id.dialog_datepicker_datepicker);
        //tvDay = (TextView) v.findViewById(R.id.dialog_datepicker_day);

        v.findViewById(R.id.dialog_datepicker_bSave).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //dateListener.onSetDate(calendar.getTime());
                        sendResult(CalendarDateFormat.toString(calendar.getTime()));
                        dismiss();
                    }
                }
        );
        v.findViewById(R.id.dialog_datepicker_bClear).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //dateListener.onSetDate(null);
                        getTargetFragment().onActivityResult(getTargetRequestCode(), RESULT_CANCEL, null);
                        dismiss();
                    }
                }
        );
        calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);

        dpDatepicker.init(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH), new DatePicker.OnDateChangedListener() {
                    @Override
                    public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        calendar.set(year, monthOfYear, dayOfMonth);
                        //tvDay.setText(CalendarDateFormat.toString(calendar.getTime()));
                    }
                });


        return v;
    }

    private void sendResult(String date) {
        Intent intent = new Intent();
        intent.putExtra("DATE", date);
        getTargetFragment().onActivityResult(getTargetRequestCode(), RESULT_OK, intent);
    }

    @Override
    public void onStart() {
        super.onStart();

        //Date initDate = dateListener.onGetDate();
        Date initDate = null;
        if (initDate != null) {
            calendar.setTime(initDate);
        }
        else {
            calendar.setTime(new Date());
        }
        //tvDay.setText(CalendarDateFormat.toString(calendar.getTime()));
        dpDatepicker.updateDate(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH));
    }

    static class CalendarDateFormat {
        static final String DATE_FORMAT_STRING = "EEEE\nd MMMM y";
        static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat(DATE_FORMAT_STRING);
        static public String toString (Date d) {
            return DATE_FORMAT.format(d);
        }
    }

    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);

    }

    public void onCancel(DialogInterface dialog) {
        super.onCancel(dialog);

    }
}