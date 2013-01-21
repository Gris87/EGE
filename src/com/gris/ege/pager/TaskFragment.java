package com.gris.ege.pager;

import com.gris.ege.R;
import com.gris.ege.other.GlobalData;
import com.gris.ege.other.Task;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ViewFlipper;

public class TaskFragment extends Fragment
{
    public static final int MODE_VIEW_TASK   = 0;
    public static final int MODE_TEST_TASK   = 1;
    public static final int MODE_VIEW_RESULT = 2;



    private TextView       mTaskHeaderView;
    private TextView       mTaskStatusView;
    private ViewFlipper    mTaskViewFlipper;
    private ProgressBar    mDownloadProgressBar;
    private Button         mRetryButton;
    private ImageView      mTaskImageView;
    private TextView       mAnswerTextView;
    private RelativeLayout mBottomLayout;
    private EditText       mAnswerEditText;
    private Button         mAnswerButton;

    private Task mTask;
    private int  mMode;



    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        Bundle aArgs=getArguments();

        if (aArgs!=null)
        {
            int aTaskId=aArgs.getInt(GlobalData.TASK_ID);

            mTask=GlobalData.tasks.get(aTaskId);
            mMode=aArgs.getInt(GlobalData.MODE);
        }
        else
        {
            mTask=GlobalData.tasks.get(0);
            mMode=MODE_VIEW_TASK;
        }
    }

    @Override
    public View onCreateView(LayoutInflater aInflater, ViewGroup aContainer, Bundle aSavedInstanceState)
    {
        View aView=aInflater.inflate(R.layout.task_page_item, aContainer, false);

        // Get controls
        mTaskHeaderView      = (TextView)      aView.findViewById(R.id.taskHeaderTextView);
        mTaskStatusView      = (TextView)      aView.findViewById(R.id.taskStatusTextView);
        mTaskViewFlipper     = (ViewFlipper)   aView.findViewById(R.id.taskViewFlipper);
        mDownloadProgressBar = (ProgressBar)   aView.findViewById(R.id.downloadProgressBar);
        mRetryButton         = (Button)        aView.findViewById(R.id.retryButton);
        mTaskImageView       = (ImageView)     aView.findViewById(R.id.taskImageView);
        mAnswerTextView      = (TextView)      aView.findViewById(R.id.answerTextView);
        mBottomLayout        = (RelativeLayout)aView.findViewById(R.id.bottomLayout);
        mAnswerEditText      = (EditText)      aView.findViewById(R.id.answerEditText);
        mAnswerButton        = (Button)        aView.findViewById(R.id.answerButton);

        // Initialize controls
        mTaskHeaderView.setText(getString(R.string.task_header, mTask.getCategory(), mTask.getId()+1));
        updateStatus();

        switch (mMode)
        {
            case MODE_VIEW_TASK:
                mAnswerTextView.setVisibility(View.GONE);
            break;
            case MODE_TEST_TASK:
                mAnswerTextView.setVisibility(View.GONE);
                mAnswerButton.setVisibility(View.GONE);
            break;
            case MODE_VIEW_RESULT:
                mBottomLayout.setVisibility(View.GONE);
            break;
        }

        return aView;
    }

    public void updateStatus()
    {
        if (mMode==MODE_TEST_TASK)
        {
            mTaskStatusView.setVisibility(View.GONE);
        }
        else
        {
            mTaskStatusView.setVisibility(View.VISIBLE);

            if (mTask.isFinished())
            {
                mTaskStatusView.setText(getString(mMode==MODE_VIEW_TASK? R.string.finished : R.string.correct));
                mTaskStatusView.setTextColor(getResources().getColor(R.color.good));
            }
            else
            {
                mTaskStatusView.setText(getString(mMode==MODE_VIEW_TASK? R.string.not_finished : R.string.not_correct));
                mTaskStatusView.setTextColor(getResources().getColor(R.color.bad));
            }
        }
    }
}
