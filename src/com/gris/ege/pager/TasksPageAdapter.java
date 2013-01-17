package com.gris.ege.pager;

import java.util.ArrayList;

import com.gris.ege.other.GlobalData;
import com.gris.ege.other.Task;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class TasksPageAdapter extends FragmentPagerAdapter
{
    private ArrayList<Task> mData;
    private boolean         mWithAnswers;



    public TasksPageAdapter(FragmentManager fm, ArrayList<Task> aData, boolean aWithAnswers)
    {
        super(fm);

        mData=aData;
        mWithAnswers=aWithAnswers;
    }

    @Override
    public int getCount()
    {
        return mData.size();
    }

    @Override
    public Fragment getItem(int aPosition)
    {
        TaskFragment aFragment = new TaskFragment();

        Bundle aArgs = new Bundle();
        aArgs.putInt(    GlobalData.TASK_ID,      mData.get(aPosition).getId());
        aArgs.putBoolean(GlobalData.WITH_ANSWERS, mWithAnswers);
        aFragment.setArguments(aArgs);

        return aFragment;
    }
}