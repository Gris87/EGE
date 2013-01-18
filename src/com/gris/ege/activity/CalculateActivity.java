package com.gris.ege.activity;

import java.util.ArrayList;

import com.gris.ege.R;
import com.gris.ege.db.ResultsOpenHelper;
import com.gris.ege.other.GlobalData;
import com.gris.ege.other.Task;
import com.gris.ege.pager.TasksPageAdapter;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class CalculateActivity extends FragmentActivity
{
    private static final String TAG="CalculateActivity";



    private static final int TIMER_TICK=1;
    private static final int SELECT_PAGE=2;

    private static final int TIMER_INTERVAL=1000;



    private TextView         mTimeLeftTextView;

    private ViewPager        mTasksPager;
    private TasksPageAdapter mTasksAdapter;

    private long             mActivityStart=0;



    private Handler mHandler = new Handler()
    {
        @Override
        public void handleMessage(Message msg)
        {
            switch (msg.what)
            {
                case TIMER_TICK:
                    onTimerTick();
                break;
                case SELECT_PAGE:
                    mTasksPager.setCurrentItem(msg.arg1, false);
                break;
            }
        }
    };



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

            Message aSelectPageMessage=new Message();
            aSelectPageMessage.what=SELECT_PAGE;
            aSelectPageMessage.arg1=aTaskId;
            mHandler.sendMessage(aSelectPageMessage);

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

            mActivityStart=SystemClock.uptimeMillis();
            onTimerTick();
        }

        mTasksPager.setAdapter(mTasksAdapter);
    }

    @Override
    protected void onPause()
    {
        mHandler.removeMessages(TIMER_TICK);

        super.onPause();
    }

    @Override
    protected void onResume()
    {
        if (isInTestingMode())
        {
            onTimerTick();
        }

        super.onResume();
    }

    @Override
    public void onBackPressed()
    {
        if (isInTestingMode())
        {
            DialogFragment aFinishDialog = new DialogFragment()
            {
                public Dialog onCreateDialog(Bundle savedInstanceState)
                {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

                    builder.setMessage(R.string.do_you_want_to_finish)
                           .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener()
                           {
                               public void onClick(DialogInterface dialog, int id)
                               {
                                   completeTest();
                               }
                           })
                           .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener()
                           {
                               public void onClick(DialogInterface dialog, int id)
                               {
                                   dismiss();
                               }
                           });

                    return builder.create();
                }
            };

            aFinishDialog.show(getSupportFragmentManager(), "FinishDialog");
        }
        else
        {
            super.onBackPressed();
        }
    }

    public void onTimerTick()
    {
        long aCurTime=SystemClock.uptimeMillis();
        long aTimeLeft=GlobalData.selectedLesson.getTime()*60*1000-(aCurTime-mActivityStart);

        if (aTimeLeft<0)
        {
            completeTest();
        }
        else
        {
            aTimeLeft/=1000;

            int aSeconds=(int)(aTimeLeft % 60);

            aTimeLeft=(aTimeLeft-aSeconds) / 60;

            int aMinutes=(int)(aTimeLeft % 60);

            aTimeLeft=(aTimeLeft-aMinutes) / 60;

            int aHours=(int)aTimeLeft;

            String aHoursStr=String.valueOf(aHours);
            String aMinutesStr=String.valueOf(aMinutes);
            String aSecondsStr=String.valueOf(aSeconds);

            if (aMinutes<10)
            {
                aMinutesStr="0"+aMinutesStr;
            }

            if (aSeconds<10)
            {
                aSecondsStr="0"+aSecondsStr;
            }

            mTimeLeftTextView.setText(getString(R.string.time_left, aHoursStr, aMinutesStr, aSecondsStr));
        }

        mHandler.sendEmptyMessageDelayed(TIMER_TICK, TIMER_INTERVAL);
    }

    public void completeTest()
    {
        SQLiteDatabase aDb=null;

        try
        {
            ResultsOpenHelper aResultsHelper=new ResultsOpenHelper(this);

            SharedPreferences aSettings=getSharedPreferences(GlobalData.PREFS_NAME, 0);
            String aUserName=aSettings.getString(GlobalData.OPTION_USER_NAME, "");

            long aUserId=aResultsHelper.getOrCreateUserId(aUserName);
            long aLessonId=aResultsHelper.getOrCreateLessonId(GlobalData.selectedLesson.getId());

            // ------------------------------------------------------------

            aDb=aResultsHelper.getWritableDatabase();

            long aTimeForExam=SystemClock.uptimeMillis()-mActivityStart;

            if (aTimeForExam>GlobalData.selectedLesson.getTime()*60*1000)
            {
                aTimeForExam=GlobalData.selectedLesson.getTime()*60*1000;
            }

            ContentValues aResultValues=new ContentValues();
            aResultValues.put(ResultsOpenHelper.COLUMN_USER_ID,   aUserId);
            aResultValues.put(ResultsOpenHelper.COLUMN_LESSON_ID, aLessonId);
            aResultValues.put(ResultsOpenHelper.COLUMN_TIME,      aTimeForExam);
            aResultValues.put(ResultsOpenHelper.COLUMN_PERCENT,   100); // TODO: Not 100 %

            long aResultId=aDb.insertOrThrow(
                                             ResultsOpenHelper.RESULTS_TABLE_NAME,
                                             null,
                                             aResultValues
                                            );

            // ------------------------------------------------------------

            Intent aData=new Intent();
            aData.putExtra(GlobalData.RESULT_ID, aResultId);
            setResult(StartTestActivity.RESULT_START_TEST, aData);
        }
        catch (Exception e)
        {
            Log.e(TAG, "Problem occured while saving data to database", e);
        }

        if (aDb!=null)
        {
            aDb.close();
        }

        finish();
    }

    public boolean isInTestingMode()
    {
        return mTimeLeftTextView.getVisibility()==View.VISIBLE;
    }
}
