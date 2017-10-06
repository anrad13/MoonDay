package ru.anrad.moonday;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import ru.anrad.moonday.dao.HistoryDataSource;
import ru.anrad.moonday.dao.MoonDayStatistic;
import ru.anrad.moonday.dao.Statistic;
import ru.anrad.moonday.dao.StatusService;

public class StatisticActivity extends AppCompatActivity {

    //private StatusService statusService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistic);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        TextView hotDays = (TextView) findViewById(R.id.activity_statistic_hot);
        TextView restDays = (TextView) findViewById(R.id.activity_statistic_rest);

        //MoonDayStatistic stat = HistoryDataSource.getInstance(this).getStatistic();
        Statistic stat = new StatusService(this).getStatistic();

        if (stat.isHasRed()) {
            hotDays.setText(stat.getRedDays() + " дней");
        } else {
            hotDays.setText("Недостаточно данных");
        }

        if (stat.isHasGreen()) {
            restDays.setText(stat.getGreenDays() + " дней");
        } else {
            restDays.setText("Недостаточно данных");
        }
    }
}
