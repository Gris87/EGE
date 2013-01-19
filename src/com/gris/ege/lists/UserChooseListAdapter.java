package com.gris.ege.lists;

import com.gris.ege.R;
import com.gris.ege.db.ResultsOpenHelper;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class UserChooseListAdapter extends CursorAdapter
{
    private int mUserIndex;



    private static class ViewHolder
    {
        TextView mUserName;
    }



    public UserChooseListAdapter(Context aContext, Cursor aCursor)
    {
        super(aContext, aCursor, false);
        getColumnIndices(aCursor);
    }

    private void getColumnIndices(Cursor aCursor)
    {
        if (aCursor!=null)
        {
            mUserIndex=aCursor.getColumnIndexOrThrow(ResultsOpenHelper.COLUMN_USER_NAME);
        }
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent)
    {
        LayoutInflater aLayoutInflater = LayoutInflater.from(context);

        View aView=aLayoutInflater.inflate(R.layout.user_list_item, parent, false);

        ViewHolder aHolder=new ViewHolder();
        aHolder.mUserName=(TextView)aView.findViewById(R.id.userNameTextView);
        aView.setTag(aHolder);

        return aView;
    }

    @Override
    public void bindView(View aView, Context aContext, Cursor aCursor)
    {
        ViewHolder aHolder=(ViewHolder)aView.getTag();

        aHolder.mUserName.setText(aCursor.getString(mUserIndex));
    }

    @Override
    public void changeCursor(Cursor aCursor)
    {
        super.changeCursor(aCursor);
        getColumnIndices(aCursor);
    }
}