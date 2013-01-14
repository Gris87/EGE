package com.gris.ege.lists;

import com.gris.ege.R;
import com.gris.ege.db.TasksDatabase;

import android.content.Context;
import android.database.Cursor;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ResourceCursorAdapter;
import android.widget.TextView;

public class TasksListAdapter extends ResourceCursorAdapter
{
    private TasksDatabase mDb;

	private int mIDIndex;
    private int mCategoryIndex;

    private String mSelectedLesson;

    private static class ViewHolder
    {
        TextView category;
        TextView status;
    }

    public TasksListAdapter(Context context, int layout, Cursor cursor, String[] from, int[] to, String aSelectedLesson)
    {
        super(context, layout, cursor, true);
        mSelectedLesson=aSelectedLesson;

        mDb=new TasksDatabase(context, mSelectedLesson);

        getColumnIndices(cursor);
    }

    private void getColumnIndices(Cursor cursor)
    {
        if (cursor != null)
        {
            mIDIndex = cursor.getColumnIndexOrThrow(TasksDatabase.COLUMN_ID);
            mCategoryIndex = cursor.getColumnIndexOrThrow(TasksDatabase.COLUMN_CATEGORY);
        }
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent)
    {
        View v=super.newView(context, cursor, parent);

        ViewHolder vh=new ViewHolder();
        vh.category=(TextView) v.findViewById(R.id.categoryTextView);
        vh.status=(TextView) v.findViewById(R.id.statusTextView);
        v.setTag(vh);

        return v;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor)
    {
        ViewHolder vh=(ViewHolder)view.getTag();

        vh.category.setText(cursor.getString(mIDIndex)+". "+cursor.getString(mCategoryIndex));
        vh.status.setText("Not finished");
    }

    @Override
    public void changeCursor(Cursor cursor)
    {
        super.changeCursor(cursor);
        getColumnIndices(cursor);
    }

    public void takeTaskCursor()
    {
        changeCursor(mDb.tasksCursor());
    }
}