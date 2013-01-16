package com.gris.ege.activity;

import com.gris.ege.R;
import com.gris.ege.other.GlobalData;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.app.Activity;

public class StartTestActivity extends Activity implements OnClickListener
{
    private static final String TAG="StartTestActivity";

    private TextView mTimeTextView;
    private TextView mTasksATextView;
    private TextView mTasksBTextView;
    private TextView mTasksCTextView;
    private TextView mTotalTasksTextView;

    private Button   mStartButton;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_start_test);

        Log.v(TAG, "Start testing for lesson \""+GlobalData.selectedLesson.getId()+"\"");

        // Get controls
        mTimeTextView       = (TextView)findViewById(R.id.timeTextView);
        mTasksATextView     = (TextView)findViewById(R.id.tasksATextView);
        mTasksBTextView     = (TextView)findViewById(R.id.tasksBTextView);
        mTasksCTextView     = (TextView)findViewById(R.id.tasksCTextView);
        mTotalTasksTextView = (TextView)findViewById(R.id.totalTasksTextView);
        mStartButton        = (Button)findViewById(R.id.startButton);

        // Set listeners
        mStartButton.setOnClickListener(this);

        int aMinutes = GlobalData.selectedLesson.getTime() % 60;
        int aHours   = (GlobalData.selectedLesson.getTime()-aMinutes) / 60;

        mTimeTextView.setText(getString(R.string.time_for_exam, aHours, aMinutes, 0));

        mTasksATextView.setText(getString(R.string.A_task_count, 0));
        mTasksBTextView.setText(getString(R.string.B_task_count, 0));
        mTasksCTextView.setText(getString(R.string.C_task_count, 0));
        mTotalTasksTextView.setText(getString(R.string.total_task_count, 0));

    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.startButton:
            break;
        }
    }
}
