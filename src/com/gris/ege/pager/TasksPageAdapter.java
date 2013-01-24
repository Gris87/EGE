package com.gris.ege.pager;

import java.util.ArrayList;

import com.gris.ege.other.GlobalData;
import com.gris.ege.other.Task;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.SparseArray;
import android.view.ViewGroup;

public class TasksPageAdapter extends FragmentStatePagerAdapter
{
    private SparseArray<TaskFragment> mPageReferenceMap = new SparseArray<TaskFragment>();

    private ArrayList<Task> mData;



    public TasksPageAdapter(FragmentManager fm, ArrayList<Task> aData)
    {
        super(fm);

        mData=aData;
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
        aArgs.putInt(GlobalData.TASK_ID, mData.get(aPosition).getId());
        aFragment.setArguments(aArgs);

        mPageReferenceMap.put(aPosition, aFragment);

        return aFragment;
    }

    @Override
    public void destroyItem(ViewGroup aContainer, int aPosition, Object aObject)
    {
        mPageReferenceMap.remove(aPosition);
        super.destroyItem(aContainer, aPosition, aObject);
    }

    public TaskFragment getFragment(int aPosition)
    {
        return mPageReferenceMap.get(aPosition);
    }

    public ArrayList<Task> getData()
    {
        return mData;
    }
}