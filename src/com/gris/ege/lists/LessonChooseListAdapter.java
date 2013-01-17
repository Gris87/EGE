package com.gris.ege.lists;

import com.gris.ege.R;
import com.gris.ege.other.GlobalData;
import com.gris.ege.other.Lesson;

import android.content.Context;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class LessonChooseListAdapter extends BaseAdapter
{
    private Context mContext;



    private static class ViewHolder
    {
        TextView mName;
        TextView mId;
    }

    public LessonChooseListAdapter(Context aContext)
    {
        super();

        mContext=aContext;
    }

    @Override
    public int getCount()
    {
        return GlobalData.lessons.size();
    }

    @Override
    public Object getItem(int aPosition)
    {
        return aPosition>=0 && aPosition<GlobalData.lessons.size() ? GlobalData.lessons.get(aPosition) : null;
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

        Lesson aBindComp=GlobalData.lessons.get(aPosition);

        aHolder.mName.setText(aBindComp.getName());
        aHolder.mId.setText(Environment.getExternalStorageDirectory().getPath()+GlobalData.PATH_ON_SD_CARD+aBindComp.getId());
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
