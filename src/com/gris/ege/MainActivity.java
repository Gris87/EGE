package com.gris.ege;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends Activity implements OnClickListener
{
    private final int CHOOSE_VIEW_TASKS   = 0;
    private final int CHOOSE_START_TEST   = 1;
    private final int CHOOSE_VIEW_RESULTS = 2;

    EditText mNameEditText;

    Button mViewTasksButton;
    Button mStartTestButton;
    Button mViewResultsButton;

	@Override
    protected void onCreate(Bundle savedInstanceState)
    {
	    super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_main);

		mNameEditText = (EditText)findViewById(R.id.nameEditText);
		mViewTasksButton = (Button)findViewById(R.id.viewTasksButton);
		mStartTestButton = (Button)findViewById(R.id.startTestButton);
		mViewResultsButton = (Button)findViewById(R.id.viewResultsButton);

		mViewTasksButton.setOnClickListener(this);
		mStartTestButton.setOnClickListener(this);
		mViewResultsButton.setOnClickListener(this);
	}

	public void makeChoose(int aChoose)
	{
	    if (mNameEditText.length()>0)
	    {

	    }
	    else
	    {
	        Toast.makeText(this, R.string.name_is_empty, Toast.LENGTH_LONG).show();
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
