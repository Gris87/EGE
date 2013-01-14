package com.gris.ege.activity;

import java.util.ArrayList;

import com.gris.ege.R;
import com.gris.ege.other.Lesson;
import com.gris.ege.other.LessonsParser;

import android.app.Activity;
import android.content.Intent;
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

    public static final String OPTION_USER_NAME       = "userName";
    public static final String OPTION_SELECTED_LESSON = "selectedLesson";

    private static final int CHOICE_VIEW_TASKS     = 0;
    private static final int CHOICE_START_TEST     = 1;
    private static final int CHOICE_VIEW_RESULTS   = 2;

    private static final int REQUEST_LESSON_SELECT = 1;
    public  static final int RESULT_LESSON_SELECT  = 1;



    private static ArrayList<Lesson> mLessons;

    private EditText          mNameEditText;

    private Button            mLessonButton;

    private Button            mViewTasksButton;
    private Button            mStartTestButton;
    private Button            mViewResultsButton;



	@Override
    protected void onCreate(Bundle savedInstanceState)
    {
	    super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_main);

		// Initialize variables
		mLessons=new LessonsParser().parse(this);

		// Get controls
		mNameEditText = (EditText)findViewById(R.id.nameEditText);
		mLessonButton = (Button)findViewById(R.id.lessonButton);
		mViewTasksButton = (Button)findViewById(R.id.viewTasksButton);
		mStartTestButton = (Button)findViewById(R.id.startTestButton);
		mViewResultsButton = (Button)findViewById(R.id.viewResultsButton);

		// Set listeners
		mLessonButton.setOnClickListener(this);
		mViewTasksButton.setOnClickListener(this);
		mStartTestButton.setOnClickListener(this);
		mViewResultsButton.setOnClickListener(this);

		// Restore preferences
	    SharedPreferences aSettings = getSharedPreferences(PREFS_NAME, 0);

	    String aUserName       = aSettings.getString(OPTION_USER_NAME, "");
	    String aSelectedLesson = aSettings.getString(OPTION_SELECTED_LESSON, "");

	    mNameEditText.setText(aUserName);
	    selectLesson(aSelectedLesson);
	}

	@Override
	protected void onDestroy()
	{
	    saveUserName();

	    super.onDestroy();
	}

	public void saveUserName()
	{
	    SharedPreferences aSettings = getSharedPreferences(PREFS_NAME, 0);
        SharedPreferences.Editor aEditor = aSettings.edit();
        aEditor.putString(OPTION_USER_NAME, mNameEditText.getText().toString());
        aEditor.commit();
	}

	public void selectLesson(String aId)
    {
	    int index=-1;

        for (int i=0; i<mLessons.size(); ++i)
        {
            if (mLessons.get(i).getId().equals(aId))
            {
                index=i;
                break;
            }
        }

        if (index<0)
        {
            index=0;
        }

        mLessonButton.setText(mLessons.get(index).getName());



        // Save preferences
        SharedPreferences aSettings = getSharedPreferences(PREFS_NAME, 0);
        SharedPreferences.Editor aEditor = aSettings.edit();
        aEditor.putString(OPTION_SELECTED_LESSON, aId);
        aEditor.commit();
    }

	public void chooseLesson()
    {
	    saveUserName();

	    Intent aLessonSelectIntent=new Intent();
        aLessonSelectIntent.setClass(this, LessonChooseActivity.class);
        startActivityForResult(aLessonSelectIntent, REQUEST_LESSON_SELECT);
    }

	public void makeChoose(int aChoice)
	{
	    if (mNameEditText.length()>0)
	    {
	        saveUserName();

	        switch (aChoice)
	        {
	            case CHOICE_VIEW_TASKS:
	            {
	               Intent aViewTasksIntent=new Intent();
                   aViewTasksIntent.setClass(this, ViewTasksActivity.class);
                   startActivity(aViewTasksIntent);
	            }
	            break;
	            case CHOICE_START_TEST:
	            {
	               Intent aStartTestIntent=new Intent();
                   aStartTestIntent.setClass(this, StartTestActivity.class);
                   startActivity(aStartTestIntent);
	            }
                break;
                case CHOICE_VIEW_RESULTS:
                {
                   Intent aViewResultsIntent=new Intent();
                   aViewResultsIntent.setClass(this, ViewResultsActivity.class);
                   startActivity(aViewResultsIntent);
                }
                break;
	        }
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
            case R.id.lessonButton:
                chooseLesson();
            break;
            case R.id.viewTasksButton:
                makeChoose(CHOICE_VIEW_TASKS);
            break;
            case R.id.startTestButton:
                makeChoose(CHOICE_START_TEST);
            break;
            case R.id.viewResultsButton:
                makeChoose(CHOICE_VIEW_RESULTS);
            break;
        }
	}

    @Override
    protected void onActivityResult(int aRequestCode, int aResultCode, Intent aData)
    {
        switch (aRequestCode)
        {
            case REQUEST_LESSON_SELECT:
            {
                switch (aResultCode)
                {
                    case RESULT_LESSON_SELECT:
                    {
                        String aId=aData.getStringExtra("ID");
                        selectLesson(aId);
                    }
                    break;
                }
            }
            break;
        }
    }

    public static ArrayList<Lesson> getLessons()
    {
        return mLessons;
    }
}
