package ru.anrad.moonday;

import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import ru.anrad.moonday.dao.HistoryDataSource;
import ru.anrad.moonday.dao.MoonDayStatistic;

public class StatisticActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistic);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        TextView hotDays = (TextView) findViewById(R.id.activity_statistic_hot);
        TextView restDays = (TextView) findViewById(R.id.activity_statistic_rest);

        MoonDayStatistic stat = HistoryDataSource.getInstance(this).getStatistic();
        if (stat != null) {
            hotDays.setText(stat.getHotDurationDay() + " дней");
            restDays.setText(stat.getRestDurationDay() + " дней");
        } else {
            hotDays.setText("??? дней");
            restDays.setText("??? дней");
        }
    }
}
