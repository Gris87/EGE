package com.gris.ege;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.app.Activity;
import android.content.Intent;

public class LessonChooseActivity extends Activity implements ListView.OnItemClickListener
{
    private static final String TAG="LessonChooseActivity";

    private ListView                mLessonsList;

    private LessonChooseListAdapter mLessonsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        Log.v(TAG, "Start lesson selection");

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_lesson_choose);

        // Get controls
        mLessonsList=(ListView)findViewById(R.id.lessonsListView);

        // Set listeners
        mLessonsList.setOnItemClickListener(this);

        // Initialize controls
        mLessonsAdapter=new LessonChooseListAdapter(this, MainActivity.getLessons());
        mLessonsList.setAdapter(mLessonsAdapter);
    }

    @Override
    public void onItemClick(AdapterView<?> aParent, View aView, int aPosition, long aId)
    {
        switch (aParent.getId())
        {
            case R.id.lessonsListView:
            {
                String aLessonId=((Lesson)(mLessonsAdapter.getItem(aPosition))).getId();
                Log.d(TAG, "Selected lesson: "+aLessonId);

                Intent aRes=new Intent();
                aRes.putExtra("ID", aLessonId);
                setResult(MainActivity.RESULT_LESSON_SELECT, aRes);

                finish();
            }
            break;
        }
    }
}
