package com.gris.ege;

import java.util.ArrayList;

import android.content.Context;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class LessonChooseListAdapter extends BaseAdapter
{
	private ArrayList<Lesson> mData;
    private Context mContext;

    private static class ViewHolder
    {
        TextView mName;
        TextView mId;
    }

    public LessonChooseListAdapter(Context aContext, ArrayList<Lesson> aData)
    {
    	mData = aData;
    	mContext = aContext;
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

    	View aView=aLayoutInflater.inflate(R.layout.lesson_list_item, parent, false);

    	ViewHolder aHolder=new ViewHolder();

    	aHolder.mName = (TextView)aView.findViewById(R.id.nameTextView);
    	aHolder.mId   = (TextView)aView.findViewById(R.id.idTextView);

    	aView.setTag(aHolder);

        return aView;
    }

    private void bindView(int aPosition, View aView)
    {
    	ViewHolder aHolder=(ViewHolder)aView.getTag();

    	Lesson aBindComp=mData.get(aPosition);

    	aHolder.mName.setText(aBindComp.getName());
    	aHolder.mId.setText(Environment.getExternalStorageDirectory().getPath()+"/EGE/"+aBindComp.getId());
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