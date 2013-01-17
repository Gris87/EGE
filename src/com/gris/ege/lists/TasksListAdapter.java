package com.gris.ege.lists;

import java.util.ArrayList;

import com.gris.ege.R;
import com.gris.ege.other.Task;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class TasksListAdapter extends BaseAdapter
{
    private ArrayList<Task> mData;
    private Context mContext;



    private static class ViewHolder
    {
        TextView mCategory;
        TextView mStatus;
    }

    public TasksListAdapter(Context aContext, ArrayList<Task> aData)
    {
        super();

        mData=aData;
        mContext=aContext;
    }

    @Override
    public int getCount()
    {
        return mData.size();
    }

    @Override
    public Object getItem(int aPosition)
    {
        return aPosition>=0 && aPosition<mData.size() ? mData.get(aPosition) : null;
    }

    @Override
    public long getItemId(int aPosition)
    {
        return aPosition;
    }

    private View newView(Context context, ViewGroup parent)
    {
        LayoutInflater aLayoutInflater = LayoutInflater.from(context);

        View aView=aLayoutInflater.inflate(R.layout.task_list_item, parent, false);

        ViewHolder aHolder=new ViewHolder();

        aHolder.mCategory=(TextView) aView.findViewById(R.id.categoryTextView);
        aHolder.mStatus=(TextView) aView.findViewById(R.id.statusTextView);

        aView.setTag(aHolder);

        return aView;
    }

    private void bindView(int aPosition, View aView)
    {
        ViewHolder aHolder=(ViewHolder)aView.getTag();

        Task aBindComp=mData.get(aPosition);

        aHolder.mCategory.setText(String.valueOf(aBindComp.getId()+1)+". "+aBindComp.getCategory());

        if (aBindComp.isFinished())
        {
            aHolder.mStatus.setText("");
        }
        else
        {
            aHolder.mStatus.setText(mContext.getString(R.string.not_finished));
        }
    }

    @Override
    public View getView(int aPosition, View aConvertView, ViewGroup aParent)
    {
        View view=null;

        if (aConvertView!=null)
        {
            view=aConvertView;
        }
        else
        {
            view=newView(mContext, aParent);
        }

        bindView(aPosition, view);

        return view;
    }
}
