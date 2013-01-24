package com.gris.ege.activity;

import java.util.ArrayList;
import java.util.Random;

import com.gris.ege.R;
import com.gris.ege.other.GlobalData;
import com.gris.ege.other.Task;
import com.gris.ege.other.Utils;

import android.os.Bundle;
import com.gris.ege.other.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.app.Activity;
import android.content.Intent;

public class StartTestActivity extends Activity implements OnClickListener
{
    private static final String TAG   = "StartTestActivity";

    private static final String COUNT = "count";

    private static final int REQUEST_START_TEST = 1;
    public  static final int RESULT_START_TEST  = 1;



    private TextView mTimeTextView;
    private TextView mTasksATextView;
    private TextView mTasksBTextView;
    private TextView mTasksCTextView;
    private TextView mTotalTasksTextView;

    private Button   mStartButton;

    private int      mCounts[]={0, 0, 0};



    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_start_test);

        if (savedInstanceState==null)
        {
            Log.v(TAG, "Start testing for lesson \""+GlobalData.selectedLesson.getId()+"\"");
        }

        setTitle(getString(R.string.title_activity_start_testing, GlobalData.selectedLesson.getName()));

        // Get controls
        mTimeTextView       = (TextView)findViewById(R.id.timeTextView);
        mTasksATextView     = (TextView)findViewById(R.id.tasksATextView);
        mTasksBTextView     = (TextView)findViewById(R.id.tasksBTextView);
        mTasksCTextView     = (TextView)findViewById(R.id.tasksCTextView);
        mTotalTasksTextView = (TextView)findViewById(R.id.totalTasksTextView);
        mStartButton        = (Button)  findViewById(R.id.startButton);

        // Set listeners
        mStartButton.setOnClickListener(this);

        // Initialize controls
        mTimeTextView.setText(Utils.timeToString(getString(R.string.time_for_exam), GlobalData.selectedLesson.getTime()*60*1000));

        if (savedInstanceState!=null)
        {
            for (int i=0; i<mCounts.length; ++i)
            {
                mCounts[i]=savedInstanceState.getInt(COUNT+"_"+String.valueOf(i), 0);
            }
        }
        else
        {
            // Calculate counts
            String aCategories[]={"A", "B", "C"};

            for (int i=0; i<aCategories.length; ++i)
            {
                do
                {
                    boolean good=false;
                    ++mCounts[i];

                    for (int j=0; j<GlobalData.tasks.size(); ++j)
                    {
                        if (GlobalData.tasks.get(j).getCategory().equals(aCategories[i]+String.valueOf(mCounts[i])))
                        {
                            good=true;
                            break;
                        }
                    }

                    if (!good)
                    {
                        break;
                    }

                } while(true);

                --mCounts[i];
            }
        }

        mTasksATextView.setText(getString(R.string.A_task_count, mCounts[0]));
        mTasksBTextView.setText(getString(R.string.B_task_count, mCounts[1]));
        mTasksCTextView.setText(getString(R.string.C_task_count, mCounts[2]));

        int aTotalCount=0;

        for (int i=0; i<mCounts.length; ++i)
        {
            aTotalCount+=mCounts[i];
        }

        mTotalTasksTextView.setText(getString(R.string.total_task_count, aTotalCount));
    }

    @Override
    protected void onSaveInstanceState(Bundle aOutState)
    {
        for (int i=0; i<mCounts.length; ++i)
        {
            aOutState.putInt(COUNT+"_"+String.valueOf(i), mCounts[i]);
        }

        super.onSaveInstanceState(aOutState);
    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.startButton:
                ArrayList<Task> aSelectedTasks=new ArrayList<Task>();
                Random aRandom=new Random();

                String aCategories[]={"A", "B", "C"};

                for (int i=0; i<aCategories.length; ++i)
                {
                    int aIndexInCategory=0;

                    do
                    {
                        ArrayList<Task> aCategoryTasks=new ArrayList<Task>();
                        ++aIndexInCategory;

                        for (int j=0; j<GlobalData.tasks.size(); ++j)
                        {
                            if (GlobalData.tasks.get(j).getCategory().equals(aCategories[i]+String.valueOf(aIndexInCategory)))
                            {
                                aCategoryTasks.add(GlobalData.tasks.get(j));
                            }
                        }

                        if (aCategoryTasks.size()==0)
                        {
                            break;
                        }

                        aSelectedTasks.add(aCategoryTasks.get(aRandom.nextInt(aCategoryTasks.size())));
                    } while(true);
                }


                // Start calculation activity
                Intent aCalculateIntent=new Intent();
                aCalculateIntent.setClass(this, CalculateActivity.class);
                aCalculateIntent.putExtra(GlobalData.TASKS_COUNT, aSelectedTasks.size());

                for (int i=0; i<aSelectedTasks.size(); ++i)
                {
                    aCalculateIntent.putExtra(GlobalData.TASK_ID+"_"+String.valueOf(i), aSelectedTasks.get(i).getId());
                }

                startActivityForResult(aCalculateIntent, REQUEST_START_TEST);
            break;
        }
    }

    @Override
    protected void onActivityResult(int aRequestCode, int aResultCode, Intent aData)
    {
        switch (aRequestCode)
        {
            case REQUEST_START_TEST:
            {
                switch (aResultCode)
                {
                    case RESULT_START_TEST:
                    {
                    	long aResultId=aData.getLongExtra(GlobalData.RESULT_ID, -1);

                    	Intent aCalculateIntent=new Intent();
                        aCalculateIntent.setClass(this, CalculateActivity.class);
                        aCalculateIntent.putExtra(GlobalData.RESULT_ID, aResultId);
                        startActivity(aCalculateIntent);
                    }
                    break;
                }

                finish();
            }
            break;
        }
    }
}
