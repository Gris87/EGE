package com.gris.ege.pager;

import com.gris.ege.R;
import com.gris.ege.other.GlobalData;
import com.gris.ege.other.Task;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class TaskFragment extends Fragment
{
    private Task mTask;

    private TextView mTaskHeaderView;
    private TextView mTaskStatusView;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        int aTaskId;

        Bundle aArgs=getArguments();

        if (aArgs!=null)
        {
            aTaskId=aArgs.getInt(GlobalData.TASK_ID);
        }
        else
        {
            aTaskId=0;
        }

        mTask=GlobalData.tasks.get(aTaskId);
    }

    @Override
    public View onCreateView(LayoutInflater aInflater, ViewGroup aContainer, Bundle aSavedInstanceState)
    {
        View aView=aInflater.inflate(R.layout.task_page_item, aContainer, false);

        mTaskHeaderView=(TextView)aView.findViewById(R.id.taskHeaderTextView);
        mTaskStatusView=(TextView)aView.findViewById(R.id.taskStatusTextView);

        mTaskHeaderView.setText(getString(R.string.task_header, mTask.getId()+1, mTask.getCategory()));
        updateStatus();

        return aView;
    }

    public void updateStatus()
    {
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
}
