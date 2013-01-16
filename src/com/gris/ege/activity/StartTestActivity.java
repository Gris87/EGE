package com.gris.ege.activity;

import com.gris.ege.R;
import com.gris.ege.other.GlobalData;

import android.os.Bundle;
import android.util.Log;
import android.app.Activity;

public class StartTestActivity extends Activity
{
    private static final String TAG="StartTestActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_start_test);

        Log.v(TAG, "Start testing for lesson \""+GlobalData.selectedLesson+"\"");
    }
}
