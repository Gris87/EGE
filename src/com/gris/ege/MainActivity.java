package com.gris.ege;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends Activity implements OnClickListener
{
    public static final String PREFS_NAME = "Settings";

    private static final int CHOOSE_VIEW_TASKS   = 0;
    private static final int CHOOSE_START_TEST   = 1;
    private static final int CHOOSE_VIEW_RESULTS = 2;

    private EditText mNameEditText;

    private Button mViewTasksButton;
    private Button mStartTestButton;
    private Button mViewResultsButton;

	@Override
    protected void onCreate(Bundle savedInstanceState)
    {
	    super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_main);

		// Get controls
		mNameEditText = (EditText)findViewById(R.id.nameEditText);
		mViewTasksButton = (Button)findViewById(R.id.viewTasksButton);
		mStartTestButton = (Button)findViewById(R.id.startTestButton);
		mViewResultsButton = (Button)findViewById(R.id.viewResultsButton);

		// Set listeners
		mViewTasksButton.setOnClickListener(this);
		mStartTestButton.setOnClickListener(this);
		mViewResultsButton.setOnClickListener(this);

		// Restore preferences
	    SharedPreferences aSettings = getSharedPreferences(PREFS_NAME, 0);
	    String aUserName = aSettings.getString("userName", "");

	    mNameEditText.setText(aUserName);
	}

	public void makeChoose(int aChoose)
	{
	    if (mNameEditText.length()>0)
	    {
	        // Save preferences
	        SharedPreferences aSettings = getSharedPreferences(PREFS_NAME, 0);
	        SharedPreferences.Editor aEditor = aSettings.edit();
	        aEditor.putString("userName", mNameEditText.getText().toString());
	        aEditor.commit();
	    }
	    else
	    {
	        Toast.makeText(this, R.string.name_is_empty, Toast.LENGTH_SHORT).show();
	    }
	}

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.viewTasksButton:
                makeChoose(CHOOSE_VIEW_TASKS);
            break;
            case R.id.startTestButton:
                makeChoose(CHOOSE_START_TEST);
            break;
            case R.id.viewResultsButton:
                makeChoose(CHOOSE_VIEW_RESULTS);
            break;
        }
	}

}
