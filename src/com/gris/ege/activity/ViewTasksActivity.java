package com.gris.ege.activity;

import com.gris.ege.R;
import com.gris.ege.lists.TasksListAdapter;
import com.gris.ege.other.GlobalData;
import com.gris.ege.other.Task;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.app.Activity;
import android.content.Intent;

public class ViewTasksActivity extends Activity implements ListView.OnItemClickListener
{
    private static final String TAG="ViewTasksActivity";

    private ListView         mTasksList;
    private TasksListAdapter mTasksAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_view_tasks);

        Log.v(TAG, "View tasks for lesson \""+GlobalData.selectedLesson+"\"");

        // Get controls
        mTasksList=(ListView)findViewById(R.id.tasksListView);

        // Set listeners
        mTasksList.setOnItemClickListener(this);

        // Initialize controls
        mTasksAdapter=new TasksListAdapter(this, GlobalData.tasks);
        mTasksList.setAdapter(mTasksAdapter);
    }

    @Override
    public void onItemClick(AdapterView<?> aParent, View aView, int aPosition, long aId)
    {
        switch (aParent.getId())
        {
            case R.id.tasksListView:
            {
                int aTaskId=((Task)(mTasksAdapter.getItem(aPosition))).getId();
                Log.d(TAG, "Starting task: "+String.valueOf(aTaskId));

                Intent aCalculateIntent=new Intent();
                aCalculateIntent.setClass(this, CalculateActivity.class);
                aCalculateIntent.putExtra(GlobalData.TASK_ID, aTaskId);
                startActivity(aCalculateIntent);
            }
            break;
        }
    }
}
