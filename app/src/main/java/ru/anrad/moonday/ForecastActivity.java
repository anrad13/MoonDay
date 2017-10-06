package ru.anrad.moonday;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import ru.anrad.moonday.dao.HistoryDataSource;
import ru.anrad.moonday.dao.Interval;
import ru.anrad.moonday.dao.MoonDay;
import ru.anrad.moonday.dao.MoonDayStatistic;
import ru.anrad.moonday.dao.StatusService;

public class ForecastActivity
        extends AppCompatActivity
        implements ForecastRVAdapter.OnListInteractionListener {

    private HistoryDataSource historyDS;
    private ArrayList<MoonDay> forecast = new ArrayList<>(12);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forecast);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        RecyclerView rView = (RecyclerView) findViewById(R.id.activity_forecast_recycler_view);
        rView.setLayoutManager(new LinearLayoutManager(this));
        rView.setAdapter(new ForecastRVAdapter(getForecast(), this));
        if (forecast.size()==0)
            Snackbar.make(findViewById(R.id.activity_forecast_recycler_view), "Недостаточно данных для прогноза", Snackbar.LENGTH_LONG).setAction("Action", null).show();

    }

    private List<Interval> getForecast() {
        /*
        forecast.clear();
         MoonDayStatistic stat = HistoryDataSource.getInstance(this).getStatistic();
        if (stat == null) return forecast;

        Date begin;
        Date end = stat.getLastDay().getEnd();
        for (int i = 0; i < 12; i++) {
            begin = stat.getBeginForecast(end);
            end = stat.getEndForecast(begin);
            forecast.add(new MoonDay(i, begin, end));
        }
        */
        return new StatusService(this).getForecast();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //getMenuInflater().inflate(R.menu.menu_history, menu);
        //return true;
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.menu_history_clear) {
            //@TODO сделать диалоговое окно подтверждения очистки всей базы данных
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onListInteraction(Interval item) {
        //Log.v(this.getClass().getName(), "onListInteraction(MoonDay):" + item.toString() );
        //TODO удалить эту возможность
    }

}
