package com.gris.ege.activity;

import com.gris.ege.R;
import com.gris.ege.other.Log;

import android.os.Bundle;
import android.preference.PreferenceActivity;

public class SettingsActivity extends PreferenceActivity
{
    private static final String TAG="SettingsActivity";



    @SuppressWarnings("deprecation")
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        if (savedInstanceState==null)
        {
            Log.v(TAG, "Settings shown for user");
        }

        addPreferencesFromResource(R.xml.pref_general);
    }
}
