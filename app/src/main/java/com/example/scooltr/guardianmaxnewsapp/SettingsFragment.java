package com.example.scooltr.guardianmaxnewsapp;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;

/**
 * Created by scool on 4/6/2017.
 */

public class SettingsFragment extends PreferenceFragment implements SharedPreferences.OnSharedPreferenceChangeListener
{
    public static final String KEY_PREF_OB = "@string/settings_order_by_key";
    public static final String KEY_PREF_KW ="@string/settings_keyword_key";

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        // Load the preferences from an XML resource
        addPreferencesFromResource(R.xml.main);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key)
    {
        //IT NEVER GETS IN HERE!
        if (key.equals(KEY_PREF_OB)||key.equals(KEY_PREF_KW))
        {
            // Set summary to be the user-description for the selected value
            Preference settingsPref = findPreference(key);
            settingsPref.setSummary(sharedPreferences.getString(key, ""));
        }
    }
    @Override
    public void onPause() {
        getPreferenceManager().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
        super.onPause();
    }
    @Override
    public void onResume() {
        super.onResume();
        getPreferenceManager().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);

    }


}