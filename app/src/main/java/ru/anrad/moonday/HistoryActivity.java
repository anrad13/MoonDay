package ru.anrad.moonday;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

//import ru.anrad.moonday.dao.HistoryDataSource;
import ru.anrad.moonday.dao.Interval;
//import ru.anrad.moonday.dao.MoonDay;
import ru.anrad.moonday.dao.StatusService;

public class HistoryActivity
        extends AppCompatActivity
        //implements MoonDayHistoryFragment.OnListFragmentInteractionListener {
        implements HistoryRVAdapter.OnListInteractionListener {

    //private HistoryDataSource historyDS;
    private StatusService statusService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //historyDS = HistoryDataSource.getInstance(this);
        statusService = new StatusService(this);

        //this.getSupportFragmentManager().beginTransaction().add(R.id.activity_history_fragment, new MoonDayHistoryFragment()).commit();
        RecyclerView rView = (RecyclerView) findViewById(R.id.activity_history_list);
        rView.setLayoutManager(new LinearLayoutManager(this));
        //rView.setAdapter(new HistoryRVAdapter(historyDS.getItems(), this));
        rView.setAdapter(new HistoryRVAdapter(statusService.getHistory(), this));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_history, menu);
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
        //TODO Сделать активити для редактирования\удаления записи в базе
    }

}
