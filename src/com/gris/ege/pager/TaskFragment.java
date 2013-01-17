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
import android.widget.TextView;

public class TaskFragment extends Fragment
{
    private TextView mTaskHeaderView;
    private TextView mTaskStatusView;
    private EditText mAnswerEditText;
    private Button   mAnswerButton;

    private Task mTask;
    private boolean mWithAnswers;



    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        int aTaskId;

        Bundle aArgs=getArguments();

        if (aArgs!=null)
        {
            aTaskId      = aArgs.getInt(    GlobalData.TASK_ID);
            mWithAnswers = aArgs.getBoolean(GlobalData.WITH_ANSWERS);
        }
        else
        {
            aTaskId=0;
            mWithAnswers=false;
        }

        mTask=GlobalData.tasks.get(aTaskId);
    }

    @Override
    public View onCreateView(LayoutInflater aInflater, ViewGroup aContainer, Bundle aSavedInstanceState)
    {
        View aView=aInflater.inflate(R.layout.task_page_item, aContainer, false);



        mTaskHeaderView = (TextView)aView.findViewById(R.id.taskHeaderTextView);
        mTaskStatusView = (TextView)aView.findViewById(R.id.taskStatusTextView);
        mAnswerEditText = (EditText)aView.findViewById(R.id.answerEditText);
        mAnswerButton   = (Button)  aView.findViewById(R.id.answerButton);



        mTaskHeaderView.setText(getString(R.string.task_header, mTask.getCategory(), mTask.getId()+1));
        updateStatus();

        if (mWithAnswers)
        {
            mAnswerButton.setVisibility(View.VISIBLE);
        }
        else
        {
            mAnswerButton.setVisibility(View.GONE);
        }

        return aView;
    }

    public void updateStatus()
    {
        if (mWithAnswers)
        {
            mTaskStatusView.setVisibility(View.VISIBLE);

            if (mTask.isFinished())
            {
                mTaskStatusView.setText(getString(R.string.finished));
                mTaskStatusView.setTextColor(getResources().getColor(R.color.good));
            }
            else
            {
                mTaskStatusView.setText(getString(R.string.not_finished));
                mTaskStatusView.setTextColor(getResources().getColor(R.color.bad));
            }
        }
        else
        {
            mTaskStatusView.setVisibility(View.GONE);
        }
    }
}
