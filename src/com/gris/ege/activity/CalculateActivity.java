package com.gris.ege.activity;

import java.util.ArrayList;

import com.gris.ege.R;
import com.gris.ege.db.ResultsOpenHelper;
import com.gris.ege.other.GlobalData;
import com.gris.ege.other.Task;
import com.gris.ege.other.Utils;
import com.gris.ege.pager.TaskFragment;
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
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

@SuppressLint("HandlerLeak")
public class CalculateActivity extends FragmentActivity
{
    private static final String TAG="CalculateActivity";



    private static final String START_TIME="startTime";

    private static final int TIMER_TICK=1;
    private static final int SELECT_PAGE=2;

    private static final int TIMER_INTERVAL=1000;



    private TextView         mTimeLeftTextView;

    private RelativeLayout   mResultsLayout;
    private TextView         mTimeTextView;
    private ProgressBar      mPercentProgressBar;
    private TextView         mPercentTextView;

    private ViewPager        mTasksPager;
    private TasksPageAdapter mTasksAdapter;

    private long             mActivityStart=0;
    private long             mUserId;
    private long             mLessonId;



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

        // Initialize variables
        ResultsOpenHelper aResultsHelper=new ResultsOpenHelper(this);

        SharedPreferences aSettings=getSharedPreferences(GlobalData.PREFS_NAME, 0);
        String aUserName=aSettings.getString(GlobalData.OPTION_USER_NAME, "");

        mUserId=aResultsHelper.getOrCreateUserId(aUserName);
        mLessonId=aResultsHelper.getOrCreateLessonId(GlobalData.selectedLesson.getId());

        // Get controls
        mTimeLeftTextView   = (TextView)      findViewById(R.id.timeLeftTextView);
        mResultsLayout      = (RelativeLayout)findViewById(R.id.resultsLayout);
        mTimeTextView       = (TextView)      findViewById(R.id.timeTextView);
        mPercentProgressBar = (ProgressBar)   findViewById(R.id.percentProgressBar);
        mPercentTextView    = (TextView)      findViewById(R.id.percentTextView);
        mTasksPager         = (ViewPager)     findViewById(R.id.tasksPager);

        // Initialize controls
        Intent aIntent=getIntent();
        Bundle aExtras=aIntent.getExtras();

        if (aExtras.containsKey(GlobalData.TASK_ID))
        {
            setTitle(getString(R.string.title_activity_calculate_tasks, GlobalData.selectedLesson.getName()));

            mTasksAdapter=new TasksPageAdapter(getSupportFragmentManager(), GlobalData.tasks, TaskFragment.MODE_VIEW_TASK, mUserId, mLessonId);

            if (savedInstanceState==null)
            {
                int aTaskId=aExtras.getInt(GlobalData.TASK_ID);
                Log.v(TAG, "Start calculation for task: "+String.valueOf(aTaskId));

                Message aSelectPageMessage=new Message();
                aSelectPageMessage.what=SELECT_PAGE;
                aSelectPageMessage.arg1=aTaskId;
                mHandler.sendMessage(aSelectPageMessage);
            }

            mTimeLeftTextView.setVisibility(View.GONE);
            mResultsLayout.setVisibility(View.GONE);
        }
        else
        if (aExtras.containsKey(GlobalData.TASKS_COUNT))
        {
            setTitle(getString(R.string.title_activity_calculate_testing, GlobalData.selectedLesson.getName()));

            int aTaskCount=aExtras.getInt(GlobalData.TASKS_COUNT);

            if (savedInstanceState==null)
            {
                Log.v(TAG, "Start calculation for tasks:");
                mActivityStart=SystemClock.uptimeMillis();
            }
            else
            {
                mActivityStart=savedInstanceState.getLong(START_TIME, SystemClock.uptimeMillis());
            }

            ArrayList<Task> aSelectedTasks=new ArrayList<Task>();

            for (int i=0; i<aTaskCount; ++i)
            {
                int aTaskId=aExtras.getInt(GlobalData.TASK_ID+"_"+String.valueOf(i));

                if (savedInstanceState==null)
                {
                    Log.v(TAG, "Task № "+String.valueOf(aTaskId));
                }

                Task aTask=GlobalData.tasks.get(aTaskId);
                aSelectedTasks.add(aTask);
            }

            mTasksAdapter=new TasksPageAdapter(getSupportFragmentManager(), aSelectedTasks, TaskFragment.MODE_TEST_TASK, mUserId, mLessonId);

            mTimeLeftTextView.setVisibility(View.VISIBLE);
            mResultsLayout.setVisibility(View.GONE);
        }
        else
        if (aExtras.containsKey(GlobalData.RESULT_ID))
        {
            setTitle(getString(R.string.title_activity_calculate_results, GlobalData.selectedLesson.getName()));

            long aResultId=aExtras.getLong(GlobalData.RESULT_ID);

            if (savedInstanceState==null)
            {
                Log.v(TAG, "View result № "+String.valueOf(aResultId));
            }

            long aTime=aResultsHelper.getTime(aResultId);
            int aPercent=aResultsHelper.getPercent(aResultId);

            mTimeTextView.setText(Utils.timeToString(getString(R.string.time_for_exam), aTime));
            mPercentProgressBar.setMax(100);
            mPercentProgressBar.setProgress(aPercent);
            mPercentTextView.setText(getString(R.string.percent, aPercent));

            ArrayList<Task> aSelectedTasks=aResultsHelper.getTasksForResult(aResultId, GlobalData.tasks);
            mTasksAdapter=new TasksPageAdapter(getSupportFragmentManager(), aSelectedTasks, TaskFragment.MODE_VIEW_RESULT, mUserId, mLessonId);

            mTimeLeftTextView.setVisibility(View.GONE);
            mResultsLayout.setVisibility(View.VISIBLE);
        }
        else
        {
            Log.e(TAG, "Unknown data received from Intent");
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
            mHandler.removeMessages(TIMER_TICK);
            onTimerTick();
        }

        super.onResume();
    }

    @Override
    protected void onSaveInstanceState(Bundle aOutState)
    {
        aOutState.putLong(START_TIME, mActivityStart);
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
            mTimeLeftTextView.setText(Utils.timeToString(getString(R.string.time_left), aTimeLeft));
        }

        mHandler.sendEmptyMessageDelayed(TIMER_TICK, TIMER_INTERVAL);
    }

    public void completeTest()
    {
        long aTimeForExam=SystemClock.uptimeMillis()-mActivityStart;

        if (aTimeForExam>GlobalData.selectedLesson.getTime()*60*1000)
        {
            aTimeForExam=GlobalData.selectedLesson.getTime()*60*1000;
        }

        if (aTimeForExam>180000) //3*60*1000
        {
            SQLiteDatabase aDb=null;

            try
            {
                ResultsOpenHelper aResultsHelper=new ResultsOpenHelper(this);

                // ------------------------------------------------------------

                aDb=aResultsHelper.getWritableDatabase();

                int aScore=0;
                int aTotalScore=0;
                ArrayList<Task> aTestTasks=new ArrayList<Task>();

                for (int i=0; i<mTasksAdapter.getCount(); ++i)
                {
                    mTasksPager.setCurrentItem(i, false);
                    TaskFragment aFragment=(TaskFragment)mTasksAdapter.getFragment(i);

                    aFragment.checkAnswer(false);

                    Task aTask=aFragment.getTask();
                    aTask.setAnswer(aFragment.getAnswer());
                    aTestTasks.add(aTask);

                    int aCurScore;

                    if (aTask.getCategory().charAt(0)=='A')
                    {
                        aCurScore=GlobalData.selectedLesson.getScoreA();
                    }
                    else
                    if (aTask.getCategory().charAt(0)=='B')
                    {
                        aCurScore=GlobalData.selectedLesson.getScoreB();
                    }
                    else
                    if (aTask.getCategory().charAt(0)=='C')
                    {
                        aCurScore=GlobalData.selectedLesson.getScoreC();
                    }
                    else
                    {
                        Log.e(TAG, "Invalid category \""+aTask.getCategory()+"\" for task № "+String.valueOf(aTask.getId()));
                        aCurScore=0;
                    }

                    if (aTask.isFinished())
                    {
                        aScore+=aCurScore;
                    }

                    aTotalScore+=aCurScore;
                }

                // ------------------------------------------------------------

                ContentValues aResultValues=new ContentValues();
                aResultValues.put(ResultsOpenHelper.COLUMN_USER_ID,   mUserId);
                aResultValues.put(ResultsOpenHelper.COLUMN_LESSON_ID, mLessonId);
                aResultValues.put(ResultsOpenHelper.COLUMN_TIME,      aTimeForExam);
                aResultValues.put(ResultsOpenHelper.COLUMN_PERCENT,   aTotalScore==0? 100 : aScore*100/aTotalScore);

                long aResultId=aDb.insertOrThrow(
                                                 ResultsOpenHelper.RESULTS_TABLE_NAME,
                                                 null,
                                                 aResultValues
                                                );

                // ------------------------------------------------------------

                for (int i=0; i<aTestTasks.size(); ++i)
                {
                    ContentValues aAnswerValues=new ContentValues();
                    aAnswerValues.put(ResultsOpenHelper.COLUMN_RESULT_ID,   aResultId);
                    aAnswerValues.put(ResultsOpenHelper.COLUMN_TASK_NUMBER, aTestTasks.get(i).getId());
                    aAnswerValues.put(ResultsOpenHelper.COLUMN_ANSWER,      aTestTasks.get(i).getAnswer());
                    aAnswerValues.put(ResultsOpenHelper.COLUMN_CORRECT,     aTestTasks.get(i).isFinished()? 1 : 0);

                    aDb.insertOrThrow(
                                      ResultsOpenHelper.ANSWERS_TABLE_NAME,
                                      null,
                                      aAnswerValues
                                     );
                }

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
        }

        finish();
    }

    public boolean isInTestingMode()
    {
        return mTimeLeftTextView.getVisibility()==View.VISIBLE;
    }
}
