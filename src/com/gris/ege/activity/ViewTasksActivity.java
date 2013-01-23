package com.gris.ege.activity;

import com.gris.ege.R;
import com.gris.ege.db.ResultsOpenHelper;
import com.gris.ege.lists.TasksListAdapter;
import com.gris.ege.other.GlobalData;
import com.gris.ege.other.Task;

import android.os.Bundle;
import com.gris.ege.other.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;

public class ViewTasksActivity extends Activity implements ListView.OnItemClickListener
{
    private static final String TAG="ViewTasksActivity";

    private static final int REQUEST_VIEW_TASK = 1;



    private ListView         mTasksList;
    private TasksListAdapter mTasksAdapter;



    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_view_tasks);

        if (savedInstanceState==null)
        {
            Log.v(TAG, "View tasks for lesson \""+GlobalData.selectedLesson.getId()+"\"");
        }

        setTitle(getString(R.string.title_activity_view_tasks, GlobalData.selectedLesson.getName()));

        // Initialize variables
        if (savedInstanceState==null)
        {
            SharedPreferences aSettings=getSharedPreferences(GlobalData.PREFS_NAME, 0);
            String aUserName=aSettings.getString(GlobalData.OPTION_USER_NAME, "");

            new ResultsOpenHelper(this).updateTasksStatus(aUserName, GlobalData.selectedLesson.getId(), GlobalData.tasks);
        }

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

                Intent aCalculateIntent=new Intent();
                aCalculateIntent.setClass(this, CalculateActivity.class);
                aCalculateIntent.putExtra(GlobalData.TASK_ID, aTaskId);
                startActivityForResult(aCalculateIntent, REQUEST_VIEW_TASK);
            }
            break;
        }
    }

    @Override
    protected void onActivityResult(int aRequestCode, int aResultCode, Intent aData)
    {
        switch (aRequestCode)
        {
            case REQUEST_VIEW_TASK:
            {
                mTasksList.invalidateViews();
            }
            break;
        }
    }
}
