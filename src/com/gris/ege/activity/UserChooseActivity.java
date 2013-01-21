package com.gris.ege.activity;

import com.gris.ege.R;
import com.gris.ege.db.ResultsOpenHelper;
import com.gris.ege.lists.UserChooseListAdapter;
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

public class UserChooseActivity extends Activity implements ListView.OnItemClickListener
{
	private static final String TAG="ViewResultsActivity";



    private ListView              mUsersList;
    private UserChooseListAdapter mUsersAdapter;

    private ResultsOpenHelper     mResultsHelper;
    private SQLiteDatabase        mDb;
    private Cursor                mCursor;



	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_user_choose);

		if (savedInstanceState==null)
        {
		    Log.v(TAG, "Start user selection");
        }

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

        mCursor=mResultsHelper.getUsersList(mDb);

        // Get controls
        mUsersList=(ListView)findViewById(R.id.usersListView);

        // Set listeners
        mUsersList.setOnItemClickListener(this);

        // Initialize controls
        mUsersAdapter=new UserChooseListAdapter(this, mCursor);
        mUsersList.setAdapter(mUsersAdapter);
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
            case R.id.usersListView:
            {
                mCursor.moveToPosition(aPosition);
                String aUserName=mCursor.getString(mCursor.getColumnIndexOrThrow(ResultsOpenHelper.COLUMN_USER_NAME));
                Log.d(TAG, "Selected user: "+aUserName);

                Intent aRes=new Intent();
                aRes.putExtra(GlobalData.USER_NAME, aUserName);
                setResult(MainActivity.RESULT_USER_SELECT, aRes);

                finish();
            }
            break;
        }
    }
}
