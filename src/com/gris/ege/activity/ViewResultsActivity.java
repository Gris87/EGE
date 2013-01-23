package com.gris.ege.activity;

import com.gris.ege.R;
import com.gris.ege.db.ResultsOpenHelper;
import com.gris.ege.lists.ResultsListAdapter;
import com.gris.ege.other.GlobalData;

import android.os.Bundle;
import com.gris.ege.other.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
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

        if (savedInstanceState==null)
        {
            Log.v(TAG, "View results for lesson \""+GlobalData.selectedLesson.getId()+"\"");
        }

        setTitle(getString(R.string.title_activity_view_results, GlobalData.selectedLesson.getName()));

        // Initialize variables
        SharedPreferences aSettings=getSharedPreferences(GlobalData.PREFS_NAME, 0);
        String aUserName=aSettings.getString(GlobalData.OPTION_USER_NAME, "");

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

        mCursor=mResultsHelper.getResults(mDb, aUserName, GlobalData.selectedLesson.getId());

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
                long aResultId=mCursor.getLong(mCursor.getColumnIndexOrThrow(ResultsOpenHelper.COLUMN_ID));

                Intent aCalculateIntent=new Intent();
                aCalculateIntent.setClass(this, CalculateActivity.class);
                aCalculateIntent.putExtra(GlobalData.RESULT_ID, aResultId);
                startActivity(aCalculateIntent);
            }
            break;
        }
    }
}
