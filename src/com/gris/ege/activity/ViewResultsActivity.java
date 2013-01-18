package com.gris.ege.activity;

import com.gris.ege.R;
import com.gris.ege.db.ResultsOpenHelper;
import com.gris.ege.lists.ResultsListAdapter;
import com.gris.ege.other.GlobalData;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class ViewResultsActivity extends Activity implements ListView.OnItemClickListener
{
    private static final String TAG="ViewResultsActivity";



    private ListView           mResultsList;
    private ResultsListAdapter mResultsAdapter;

    private ResultsOpenHelper  mResultsHelper;
    private SQLiteDatabase     mDb;
    private Cursor             mCursor;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_view_results);

        Log.v(TAG, "View results for lesson \""+GlobalData.selectedLesson.getId()+"\"");
        setTitle(getString(R.string.title_activity_view_results, GlobalData.selectedLesson.getName()));

        // Initialize variables
        mResultsHelper=new ResultsOpenHelper(this);
        mDb=null;

        try
        {
            mDb=mResultsHelper.getReadableDatabase();
        }
        catch (Exception e)
        {
            Log.e(TAG, "Impossible to get database", e);
        }

        mCursor=getResults();

        // Get controls
        mResultsList=(ListView)findViewById(R.id.resultsListView);

        // Set listeners
        mResultsList.setOnItemClickListener(this);

        // Initialize controls
        mResultsAdapter=new ResultsListAdapter(this, mCursor);
        mResultsList.setAdapter(mResultsAdapter);
    }

    @Override
    protected void onDestroy()
    {
        if (mCursor!=null)
        {
            mCursor.close();
        }

        if (mDb!=null)
        {
            mDb.close();
        }

        super.onDestroy();
    }

    @Override
    public void onItemClick(AdapterView<?> aParent, View aView, int aPosition, long aId)
    {
        switch (aParent.getId())
        {
            case R.id.resultsListView:
            {
                mCursor.moveToPosition(aPosition);
                int aResultId=mCursor.getInt(mCursor.getColumnIndexOrThrow(ResultsOpenHelper.COLUMN_ID));

                Intent aCalculateIntent=new Intent();
                aCalculateIntent.setClass(this, CalculateActivity.class);
                aCalculateIntent.putExtra(GlobalData.RESULT_ID, aResultId);
                startActivity(aCalculateIntent);
            }
            break;
        }
    }

    public Cursor getResults()
    {
        long aLessonId=mResultsHelper.getLessonId(mDb, GlobalData.selectedLesson.getId());

        Cursor res=null;

        try
        {
            String aSql="SELECT " + ResultsOpenHelper.RESULTS_TABLE_NAME + "." + ResultsOpenHelper.COLUMN_ID + ", " +
                                    ResultsOpenHelper.COLUMN_USER_NAME + ", " +
                                    ResultsOpenHelper.COLUMN_TIME + ", " +
                                    ResultsOpenHelper.COLUMN_PERCENT + " " +
                        "FROM " + ResultsOpenHelper.RESULTS_TABLE_NAME + " " +
                        "INNER JOIN " + ResultsOpenHelper.USERS_TABLE_NAME +
                                    " ON " + ResultsOpenHelper.COLUMN_USER_ID +
                                             "=" +
                                             ResultsOpenHelper.USERS_TABLE_NAME + "." + ResultsOpenHelper.COLUMN_ID + " " +
                        "WHERE " + ResultsOpenHelper.COLUMN_LESSON_ID + "=?";

            String[] aSelectionArgs={String.valueOf(aLessonId)};
            res=mDb.rawQuery(aSql, aSelectionArgs);
        }
        catch (Exception e)
        {
            Log.e(TAG, "Problem occured while getResults", e);
        }

        return res;
    }
}
