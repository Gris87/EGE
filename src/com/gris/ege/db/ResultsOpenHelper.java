package com.gris.ege.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class ResultsOpenHelper extends SQLiteOpenHelper
{
    private static final String TAG="ResultsOpenHelper";

    private static final int DATABASE_VERSION = 1;

    public  static final String COLUMN_ID          = "_id";
    public  static final String COLUMN_USER_NAME   = "_userName";

    public  static final String COLUMN_LESSON_NAME = "_lessonName";

    public  static final String COLUMN_RESULT_ID   = "_resultId";
    public  static final String COLUMN_TASK_ID     = "_taskId";
    public  static final String COLUMN_ANSWER      = "_answer";
    public  static final String COLUMN_CORRECT     = "_correct";

    public  static final String COLUMN_USER_ID     = "_userId";
    public  static final String COLUMN_LESSON_ID   = "_lessonId";
    public  static final String COLUMN_TIME        = "_time";
    public  static final String COLUMN_PERCENT     = "_percent";



    public static final String[] USERS_COLUMNS   = {
                                                        COLUMN_ID,
                                                        COLUMN_USER_NAME
                                                   };

    public static final String[] LESSONS_COLUMNS = {
                                                        COLUMN_ID,
                                                        COLUMN_LESSON_NAME
                                                   };

    public static final String[] ANSWERS_COLUMNS = {
                                                        COLUMN_ID,
                                                        COLUMN_RESULT_ID,
                                                        COLUMN_TASK_ID,
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
                                                           COLUMN_ID          + " INTEGER PRIMARY KEY, " +
                                                           COLUMN_USER_NAME   + " TEXT"                  +
                                                       ");";

    public  static final String LESSONS_TABLE_NAME   = "lessons";
    private static final String LESSONS_TABLE_CREATE = "CREATE TABLE " + LESSONS_TABLE_NAME + " " +
                                                       "(" +
                                                           COLUMN_ID          + " INTEGER PRIMARY KEY, " +
                                                           COLUMN_LESSON_NAME + " TEXT"                  +
                                                       ");";

    public  static final String ANSWERS_TABLE_NAME   = "answers";
    private static final String ANSWERS_TABLE_CREATE = "CREATE TABLE " + ANSWERS_TABLE_NAME + " " +
                                                       "(" +
                                                           COLUMN_ID          + " INTEGER PRIMARY KEY, " +
                                                           COLUMN_RESULT_ID   + " INTEGER, "             +
                                                           COLUMN_TASK_ID     + " INTEGER, "             +
                                                           COLUMN_ANSWER      + " TEXT, "                +
                                                           COLUMN_CORRECT     + " INTEGER"               +
                                                       ");";

    public  static final String RESULTS_TABLE_NAME   = "results";
    private static final String RESULTS_TABLE_CREATE = "CREATE TABLE " + RESULTS_TABLE_NAME + " " +
                                                       "(" +
                                                           COLUMN_ID          + " INTEGER PRIMARY KEY, " +
                                                           COLUMN_USER_ID     + " INTEGER, "             +
                                                           COLUMN_LESSON_ID   + " INTEGER, "             +
                                                           COLUMN_TIME        + " INTEGER, "             +
                                                           COLUMN_PERCENT     + " INTEGER"               +
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

    public long getUserId(SQLiteDatabase aDb, String aUserName)
    {
        long res=-1;
        Cursor aCursor=null;

        try
        {
            String[] aSelectionArgs={aUserName};
            aCursor=aDb.query(
                              ResultsOpenHelper.USERS_TABLE_NAME,
                              ResultsOpenHelper.USERS_COLUMNS,
                              ResultsOpenHelper.COLUMN_USER_NAME+"=?",
                              aSelectionArgs,
                              null,
                              null,
                              null
                             );

            if (aCursor!=null && aCursor.getCount()>0)
            {
                aCursor.moveToFirst();
                res=aCursor.getLong(aCursor.getColumnIndexOrThrow(ResultsOpenHelper.COLUMN_ID));
            }
        }
        catch (Exception e)
        {
            Log.e(TAG, "Problem occured while getUserId", e);
        }

        if (aCursor!=null)
        {
            aCursor.close();
        }

        return res;
    }
    
    public boolean isUserListEmpty()
    {
    	boolean res=true;
        SQLiteDatabase aDb=null;
        Cursor aCursor=null;

        try
        {
            aDb=getReadableDatabase();
                        
            aCursor=aDb.query(
                              ResultsOpenHelper.USERS_TABLE_NAME,
                              ResultsOpenHelper.USERS_COLUMNS,
                              null,
                              null,
                              null,
                              null,
                              null
                             );
            
            res=(aCursor==null || aCursor.getCount()==0);
        }
        catch (Exception e)
        {
            Log.e(TAG, "Problem occured while isUserListEmpty", e);
        }
        
        if (aCursor!=null)
        {
            aCursor.close();
        }

        if (aDb!=null)
        {
            aDb.close();
        }

        return res;
    }

    public long getUserId(String aUserName)
    {
        long res=-1;
        SQLiteDatabase aDb=null;

        try
        {
            aDb=getReadableDatabase();
            res=getUserId(aDb, aUserName);
        }
        catch (Exception e)
        {
            Log.e(TAG, "Problem occured while getUserId", e);
        }

        if (aDb!=null)
        {
            aDb.close();
        }

        return res;
    }

    public long getOrCreateUserId(String aUserName)
    {
        long res=-1;
        SQLiteDatabase aDb=null;
        Cursor aCursor=null;

        try
        {
            aDb=getWritableDatabase();

            String[] aSelectionArgs={aUserName};
            aCursor=aDb.query(
                              ResultsOpenHelper.USERS_TABLE_NAME,
                              ResultsOpenHelper.USERS_COLUMNS,
                              ResultsOpenHelper.COLUMN_USER_NAME+"=?",
                              aSelectionArgs,
                              null,
                              null,
                              null
                             );

            if (aCursor==null || aCursor.getCount()==0)
            {
                ContentValues aValues=new ContentValues();
                aValues.put(ResultsOpenHelper.COLUMN_USER_NAME, aUserName);

                res=aDb.insertOrThrow(
                                      ResultsOpenHelper.USERS_TABLE_NAME,
                                      null,
                                      aValues
                                     );
            }
            else
            {
                aCursor.moveToFirst();
                res=aCursor.getLong(aCursor.getColumnIndexOrThrow(ResultsOpenHelper.COLUMN_ID));
            }
        }
        catch (Exception e)
        {
            Log.e(TAG, "Problem occured while getOrCreateUserId", e);
        }

        if (aCursor!=null)
        {
            aCursor.close();
        }

        if (aDb!=null)
        {
            aDb.close();
        }

        return res;
    }

    public long getLessonId(SQLiteDatabase aDb, String aLessonID)
    {
        long res=-1;
        Cursor aCursor=null;

        try
        {
            String[] aSelectionArgs={aLessonID};
            aCursor=aDb.query(
                              ResultsOpenHelper.LESSONS_TABLE_NAME,
                              ResultsOpenHelper.LESSONS_COLUMNS,
                              ResultsOpenHelper.COLUMN_LESSON_NAME+"=?",
                              aSelectionArgs,
                              null,
                              null,
                              null
                             );

            if (aCursor!=null && aCursor.getCount()>0)
            {
                aCursor.moveToFirst();
                res=aCursor.getLong(aCursor.getColumnIndexOrThrow(ResultsOpenHelper.COLUMN_ID));
            }
        }
        catch (Exception e)
        {
            Log.e(TAG, "Problem occured while getLessonId", e);
        }

        if (aCursor!=null)
        {
            aCursor.close();
        }

        return res;
    }

    public long getLessonId(String aLessonID)
    {
        long res=-1;
        SQLiteDatabase aDb=null;

        try
        {
            aDb=getReadableDatabase();
            res=getLessonId(aDb, aLessonID);
        }
        catch (Exception e)
        {
            Log.e(TAG, "Problem occured while getLessonId", e);
        }

        if (aDb!=null)
        {
            aDb.close();
        }

        return res;
    }

    public long getOrCreateLessonId(String aLessonID)
    {
        long res=-1;
        SQLiteDatabase aDb=null;
        Cursor aCursor=null;

        try
        {
            aDb=getWritableDatabase();

            String[] aSelectionArgs={aLessonID};
            aCursor=aDb.query(
                              ResultsOpenHelper.LESSONS_TABLE_NAME,
                              ResultsOpenHelper.LESSONS_COLUMNS,
                              ResultsOpenHelper.COLUMN_LESSON_NAME+"=?",
                              aSelectionArgs,
                              null,
                              null,
                              null
                             );

            if (aCursor==null || aCursor.getCount()==0)
            {
                ContentValues aValues=new ContentValues();
                aValues.put(ResultsOpenHelper.COLUMN_LESSON_NAME, aLessonID);

                res=aDb.insertOrThrow(
                                      ResultsOpenHelper.LESSONS_TABLE_NAME,
                                      null,
                                      aValues
                                     );
            }
            else
            {
                aCursor.moveToFirst();
                res=aCursor.getLong(aCursor.getColumnIndexOrThrow(ResultsOpenHelper.COLUMN_ID));
            }
        }
        catch (Exception e)
        {
            Log.e(TAG, "Problem occured while getOrCreateLessonId", e);
        }

        if (aCursor!=null)
        {
            aCursor.close();
        }

        if (aDb!=null)
        {
            aDb.close();
        }

        return res;
    }
}