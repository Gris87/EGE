package com.gris.ege.activity;

import com.gris.ege.R;
import com.gris.ege.other.GlobalData;

import android.os.Bundle;
import android.util.Log;
import android.app.Activity;

public class ViewResultsActivity extends Activity
{
    private static final String TAG="ViewResultsActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_view_results);

        Log.v(TAG, "View results for lesson \""+GlobalData.selectedLesson+"\"");
    }
}
