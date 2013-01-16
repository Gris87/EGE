package com.gris.ege.pager;

import com.gris.ege.other.GlobalData;
import com.gris.ege.other.Task;

import android.os.Bundle;
import android.support.v4.app.Fragment;

public class TaskFragment extends Fragment
{
    private Task mTask;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        int aTaskId=getArguments().getInt(GlobalData.TASK_ID);

        mTask=GlobalData.tasks.get(aTaskId);
    }
}
