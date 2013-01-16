package com.gris.ege.activity;

import java.util.ArrayList;

import com.gris.ege.R;
import com.gris.ege.other.GlobalData;
import com.gris.ege.other.Task;
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

            mTasksAdapter=new TasksPageAdapter(getSupportFragmentManager(), GlobalData.tasks, true);

            mTimeLeftTextView.setVisibility(View.GONE);
        }
        else
        {
            int aTaskCount=aExtras.getInt(GlobalData.TASKS_COUNT);
            Log.v(TAG, "Start calculation for tasks:");

            ArrayList<Task> aSelectedTasks=new ArrayList<Task>();

            for (int i=0; i<aTaskCount; ++i)
            {
                int aTaskId=aExtras.getInt(GlobalData.TASK_ID+"_"+String.valueOf(i));
                Log.v(TAG, "Task № "+String.valueOf(aTaskId));

                Task aTask=GlobalData.tasks.get(aTaskId);
                aSelectedTasks.add(aTask);
            }

            mTasksAdapter=new TasksPageAdapter(getSupportFragmentManager(), aSelectedTasks, false);

            mTimeLeftTextView.setVisibility(View.VISIBLE);
        }

        mTasksPager.setAdapter(mTasksAdapter);
    }
}
