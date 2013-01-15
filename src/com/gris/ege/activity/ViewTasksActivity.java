package com.gris.ege.activity;

import java.util.ArrayList;

import com.gris.ege.R;
import com.gris.ege.lists.TasksListAdapter;
import com.gris.ege.other.Task;
import com.gris.ege.other.TasksParser;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.app.Activity;
import android.content.SharedPreferences;

public class ViewTasksActivity extends Activity implements ListView.OnItemClickListener
{
    private static final String TAG="ViewTasksActivity";

    private ListView         mTasksList;
    private TasksListAdapter mTasksAdapter;

    private String mSelectedLesson;
    private ArrayList<Task> mTasks;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_view_tasks);

        SharedPreferences aSettings = getSharedPreferences(MainActivity.PREFS_NAME, 0);
        mSelectedLesson = aSettings.getString(MainActivity.OPTION_SELECTED_LESSON, "");

        Log.v(TAG, "View tasks for lesson \""+mSelectedLesson+"\"");



        // Initialize variables
        mTasks=new TasksParser().parse(this, mSelectedLesson);

        // Get controls
        mTasksList=(ListView)findViewById(R.id.tasksListView);

        // Set listeners
        mTasksList.setOnItemClickListener(this);

        // Initialize controls
        mTasksAdapter=new TasksListAdapter(this, mTasks);
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
            }
            break;
        }
    }
}
