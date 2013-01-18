package com.gris.ege.lists;

import com.gris.ege.R;
import com.gris.ege.db.ResultsOpenHelper;
import com.gris.ege.other.GlobalData;
import com.gris.ege.other.Utils;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ProgressBar;
import android.widget.TextView;

public class ResultsListAdapter extends CursorAdapter
{
    private int mUserIndex;
    private int mTimeIndex;
    private int mPercentIndex;



    private static class ViewHolder
    {
        TextView    mUserName;
        TextView    mTime;
        ProgressBar mPercentProgress;
        TextView    mPercent;
    }



    public ResultsListAdapter(Context aContext, Cursor aCursor)
    {
        super(aContext, aCursor, false);
        getColumnIndices(aCursor);
    }

    private void getColumnIndices(Cursor aCursor)
    {
        if (aCursor != null)
        {
            mUserIndex    = aCursor.getColumnIndexOrThrow(ResultsOpenHelper.COLUMN_USER_NAME);
            mTimeIndex    = aCursor.getColumnIndexOrThrow(ResultsOpenHelper.COLUMN_TIME);
            mPercentIndex = aCursor.getColumnIndexOrThrow(ResultsOpenHelper.COLUMN_PERCENT);
        }
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent)
    {
        LayoutInflater aLayoutInflater = LayoutInflater.from(context);

        View aView=aLayoutInflater.inflate(R.layout.result_list_item, parent, false);

        ViewHolder aHolder=new ViewHolder();

        aHolder.mUserName        = (TextView)   aView.findViewById(R.id.userNameTextView);
        aHolder.mTime            = (TextView)   aView.findViewById(R.id.timeTextView);
        aHolder.mPercentProgress = (ProgressBar)aView.findViewById(R.id.percentProgressBar);
        aHolder.mPercent         = (TextView)   aView.findViewById(R.id.percentTextView);

        aView.setTag(aHolder);

        return aView;
    }

    @Override
    public void bindView(View aView, Context aContext, Cursor aCursor)
    {
        ViewHolder aHolder=(ViewHolder)aView.getTag();

        aHolder.mUserName.setText(aCursor.getString(mUserIndex));

        int aTime=Integer.parseInt(aCursor.getString(mTimeIndex));
        aHolder.mTime.setText(Utils.timeToString(aContext.getString(R.string.time), aTime));

        int aPercent=Integer.parseInt(aCursor.getString(mPercentIndex));

        aHolder.mPercentProgress.setMax(100);
        aHolder.mPercentProgress.setProgress(aPercent);

        aHolder.mPercent.setText(aContext.getString(R.string.percent, aPercent));
    }

    @Override
    public void changeCursor(Cursor aCursor)
    {
        super.changeCursor(aCursor);
        getColumnIndices(aCursor);
    }
}
