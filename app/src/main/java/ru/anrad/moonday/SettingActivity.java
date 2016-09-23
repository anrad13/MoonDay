package ru.anrad.moonday;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;


public class SettingActivity extends PreferenceActivity {

    SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sp = PreferenceManager.getDefaultSharedPreferences(this);
        String statCounter = sp.getString("statcounter","0");
        addPreferencesFromResource(R.xml.pref);
        EditTextPreference statCountEditText = (EditTextPreference) findPreference("statcounter");
        statCountEditText.setSummary("Интервал расчета (0-все значения) - " + statCounter);
    }
}
