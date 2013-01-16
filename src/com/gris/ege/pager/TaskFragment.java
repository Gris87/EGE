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

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        int aTaskId=getArguments().getInt(GlobalData.TASK_ID);

        mTask=GlobalData.tasks.get(aTaskId);
    }

    @Override
    public View onCreateView(LayoutInflater aInflater, ViewGroup aContainer, Bundle aSavedInstanceState)
    {
        View aView=aInflater.inflate(R.layout.task_page_item, aContainer, false);

        mTaskHeaderView=(TextView)aView.findViewById(R.id.taskHeaderTextView);

        mTaskHeaderView.setText(getString(R.string.task_header, mTask.getId()+1, mTask.getCategory()));

        return aView;
    }
}
