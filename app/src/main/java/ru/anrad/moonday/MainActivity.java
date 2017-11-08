package ru.anrad.moonday;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
//import android.support.v4.app.Fragment;
import android.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

//import ru.anrad.moonday.dao.CurrentDayDataSource;
import ru.anrad.moonday.dao.Status;
import ru.anrad.moonday.dao.StatusService;
import ru.anrad.moonday.dao.StatusType;

public class MainActivity extends AppCompatActivity
    implements OnFragmentInteractionListener {

    final int REQUEST_CODE_HISTORY = 1;
    final int REQUEST_CODE_SETTING = 2;
    final int REQUEST_CODE_FORECAST = 3;
    final int REQUEST_CODE_STATISTIC = 4;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // set fragment
        setMainActivityFragment();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.menu_main_settings) {
            Intent intent = new Intent(this, SettingActivity.class);
            startActivityForResult(intent, REQUEST_CODE_SETTING);
            return false;
        } else if (id == R.id.menu_main_history) {
            Intent intent = new Intent(this, HistoryActivity.class);
            startActivityForResult(intent, REQUEST_CODE_HISTORY);
            return true;
        } else if (id == R.id.menu_main_statistic) {
            Intent intent = new Intent(this, StatisticActivity.class);
            startActivityForResult(intent, REQUEST_CODE_STATISTIC);
            return true;
        } else if (id == R.id.menu_main_forecast) {
            Intent intent = new Intent(this, ForecastActivity.class);
            startActivityForResult(intent, REQUEST_CODE_FORECAST);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //@TODO понять как тут сделать правильно и надо ли жэто вообще
        if (requestCode == REQUEST_CODE_HISTORY && resultCode == RESULT_OK) {
            setMainActivityFragment();
            //Log.v(this.getClass().getName(), "onActivityResult: Fragment Replace");
        }
        if (requestCode == REQUEST_CODE_SETTING && resultCode == RESULT_OK) {

        }

    }

    public void onFragmentInteraction(int interactionCode) {
        switch (interactionCode) {
            case OnFragmentInteractionListener.DAY_HAS_BEGAN :
                updateWidgets();
                break;
            case OnFragmentInteractionListener.DAY_HAS_ENDED :
                updateWidgets();
                break;
        }
        setMainActivityFragment();
    }

    private void setMainActivityFragment() {
        /*
        CurrentDayDataSource currentDS = CurrentDayDataSource.getInstance(getApplicationContext());
        Fragment f;
        if (currentDS.isActive()) {
            f = new FinishActiveDayFragment();
        } else {
            f =  new BeginActiveDayFragment();
        }
        this.getFragmentManager().beginTransaction().replace(R.id.activity_main_fragment, f).commit();
        */
        //CurrentDayDataSource currentDS = CurrentDayDataSource.getInstance(getApplicationContext());
        StatusService statusService = new StatusService(this.getApplicationContext());
        Status currentStatus = statusService.getCurrentStatus();
        Fragment f;
        if (currentStatus.getType().equals(StatusType.RED)) {
            f = new FinishActiveDayFragment();
        } else {
            f =  new BeginActiveDayFragment();
        }
        this.getFragmentManager().beginTransaction().replace(R.id.activity_main_fragment, f).commit();

    }

    private void updateWidgets() {
        int[] ids = AppWidgetManager.getInstance(getApplication()).getAppWidgetIds(new ComponentName(getApplication(), MainWidget.class));
        MainWidget myWidget = new MainWidget();
        myWidget.onUpdate(this, AppWidgetManager.getInstance(this),ids);
    }

}
