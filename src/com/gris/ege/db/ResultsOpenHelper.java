package com.gris.ege.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class ResultsOpenHelper extends SQLiteOpenHelper
{
    private static final int DATABASE_VERSION = 1;

    public  static final String COLUMN_ID        = "_id";
    public  static final String COLUMN_NAME      = "_name";

    public  static final String COLUMN_TASK_ID   = "_taskId";
    public  static final String COLUMN_RESULT_ID = "_resultId";
    public  static final String COLUMN_ANSWER    = "_answer";
    public  static final String COLUMN_CORRECT   = "_correct";

    public  static final String COLUMN_USER_ID   = "_userId";
    public  static final String COLUMN_LESSON_ID = "_lessonId";
    public  static final String COLUMN_TIME      = "_time";
    public  static final String COLUMN_PERCENT   = "_percent";



    public static final String[] USERS_COLUMNS   = {
                                                        COLUMN_ID,
                                                        COLUMN_NAME
                                                   };

    public static final String[] LESSONS_COLUMNS = {
                                                        COLUMN_ID,
                                                        COLUMN_NAME
                                                   };

    public static final String[] ANSWERS_COLUMNS = {
                                                        COLUMN_ID,
                                                        COLUMN_TASK_ID,
                                                        COLUMN_RESULT_ID,
                                                        COLUMN_ANSWER,
                                                        COLUMN_CORRECT
                                                   };

    public static final String[] RESULTS_COLUMNS = {
                                                        COLUMN_ID,
                                                        COLUMN_USER_ID,
                                                        COLUMN_LESSON_ID,
                                                        COLUMN_TIME,
                                                        COLUMN_PERCENT
                                                   };



    public  static final String USERS_TABLE_NAME     = "users";
    private static final String USERS_TABLE_CREATE   = "CREATE TABLE " + USERS_TABLE_NAME + " " +
                                                       "(" +
                                                           COLUMN_ID        + " INTEGER PRIMARY KEY, " +
                                                           COLUMN_NAME      + " TEXT"                  +
                                                       ");";

    public  static final String LESSONS_TABLE_NAME   = "lessons";
    private static final String LESSONS_TABLE_CREATE = "CREATE TABLE " + LESSONS_TABLE_NAME + " " +
                                                       "(" +
                                                           COLUMN_ID        + " INTEGER PRIMARY KEY, " +
                                                           COLUMN_NAME      + " TEXT"                  +
                                                       ");";

    public  static final String ANSWERS_TABLE_NAME   = "answers";
    private static final String ANSWERS_TABLE_CREATE = "CREATE TABLE " + ANSWERS_TABLE_NAME + " " +
                                                       "(" +
                                                           COLUMN_ID        + " INTEGER PRIMARY KEY, " +
                                                           COLUMN_TASK_ID   + " INTEGER, "             +
                                                           COLUMN_RESULT_ID + " INTEGER, "             +
                                                           COLUMN_ANSWER    + " TEXT, "                +
                                                           COLUMN_CORRECT   + " INTEGER"               +
                                                       ");";

    public  static final String RESULTS_TABLE_NAME   = "results";
    private static final String RESULTS_TABLE_CREATE = "CREATE TABLE " + RESULTS_TABLE_NAME + " " +
                                                       "(" +
                                                           COLUMN_ID        + " INTEGER PRIMARY KEY, " +
                                                           COLUMN_USER_ID   + " INTEGER, "             +
                                                           COLUMN_LESSON_ID + " INTEGER, "             +
                                                           COLUMN_TIME      + " INTEGER, "             +
                                                           COLUMN_PERCENT   + " INTEGER"               +
                                                       ");";



    public ResultsOpenHelper(Context context)
    {
        super(context, "Results.db", null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        db.execSQL(USERS_TABLE_CREATE);
        db.execSQL(LESSONS_TABLE_CREATE);
        db.execSQL(ANSWERS_TABLE_CREATE);
        db.execSQL(RESULTS_TABLE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        // Nothing
    }
}