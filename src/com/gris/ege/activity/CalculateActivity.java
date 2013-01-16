package com.gris.ege.activity;

import com.gris.ege.R;
import com.gris.ege.other.GlobalData;
import com.gris.ege.pager.TasksPageAdapter;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.content.Intent;

public class CalculateActivity extends FragmentActivity
{
    private static final String TAG="CalculateActivity";

    private TextView         mTimeLeftTextView;

    private ViewPager        mTasksPager;
    private TasksPageAdapter mTasksAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculate);

        mTimeLeftTextView = (TextView) findViewById(R.id.timeLeftTextView);
        mTasksPager       = (ViewPager)findViewById(R.id.tasksPager);

        Intent aIntent=getIntent();
        Bundle aExtras=aIntent.getExtras();

        if (aExtras.containsKey(GlobalData.TASK_ID))
        {
            int aTaskId=aExtras.getInt(GlobalData.TASK_ID);
            Log.v(TAG, "Start calculation for task: "+String.valueOf(aTaskId));

            mTasksAdapter=new TasksPageAdapter(getSupportFragmentManager(), GlobalData.tasks);


            mTimeLeftTextView.setVisibility(View.GONE);
        }
        else
        {
            mTimeLeftTextView.setVisibility(View.VISIBLE);
        }

        mTasksPager.setAdapter(mTasksAdapter);
    }
}
