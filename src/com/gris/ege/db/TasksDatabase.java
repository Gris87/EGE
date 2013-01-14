package com.gris.ege.db;

import java.io.File;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class TasksDatabase
{
    private static final String TAG="TasksDatabase";

    public static final String TABLE_TASKS="tasks";

    public static final String COLUMN_ID="id";
    public static final String COLUMN_CATEGORY="category";
    public static final String COLUMN_ANSWER="answer";

    private SQLiteDatabase mDb;

    public TasksDatabase(Context context, String aLessonID)
    {
        mDb=SQLiteDatabase.openOrCreateDatabase(new File("android.resource://"+context.getPackageName()+ "/mathematics.db"), null);

        if (!mDb.isOpen())
        {
            Log.e(TAG, "Impossible to open database for lesson \""+aLessonID+"\"");
        }
    }

    public Cursor tasksCursor()
    {
        return mDb.rawQuery("SELECT * FROM "+TABLE_TASKS, null);
    }
}
