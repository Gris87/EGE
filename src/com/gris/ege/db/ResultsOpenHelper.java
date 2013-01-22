package com.gris.ege.db;

import java.util.ArrayList;

import com.gris.ege.other.Task;

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

    public  static final String COLUMN_USER_ID     = "_userId";
    public  static final String COLUMN_LESSON_ID   = "_lessonId";
    public  static final String COLUMN_TASK_NUMBER = "_taskNumber";

    public  static final String COLUMN_TIME        = "_time";
    public  static final String COLUMN_PERCENT     = "_percent";

    public  static final String COLUMN_RESULT_ID   = "_resultId";
    public  static final String COLUMN_ANSWER      = "_answer";
    public  static final String COLUMN_CORRECT     = "_correct";



    public static final String[] USERS_COLUMNS   = {
                                                        COLUMN_ID,
                                                        COLUMN_USER_NAME
                                                   };

    public static final String[] LESSONS_COLUMNS = {
                                                        COLUMN_ID,
                                                        COLUMN_LESSON_NAME
                                                   };

    public static final String[] TASKS_COLUMNS   = {
                                                        COLUMN_ID,
                                                        COLUMN_USER_ID,
                                                        COLUMN_LESSON_ID,
                                                        COLUMN_TASK_NUMBER
                                                   };

    public static final String[] RESULTS_COLUMNS = {
                                                        COLUMN_ID,
                                                        COLUMN_USER_ID,
                                                        COLUMN_LESSON_ID,
                                                        COLUMN_TIME,
                                                        COLUMN_PERCENT
                                                   };

    public static final String[] ANSWERS_COLUMNS = {
                                                        COLUMN_ID,
                                                        COLUMN_RESULT_ID,
                                                        COLUMN_TASK_NUMBER,
                                                        COLUMN_ANSWER,
                                                        COLUMN_CORRECT
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

    public  static final String TASKS_TABLE_NAME     = "tasks";
    private static final String TASKS_TABLE_CREATE   = "CREATE TABLE " + TASKS_TABLE_NAME + " " +
                                                       "(" +
                                                           COLUMN_ID          + " INTEGER PRIMARY KEY, " +
                                                           COLUMN_USER_ID     + " INTEGER, "             +
                                                           COLUMN_LESSON_ID   + " INTEGER, "             +
                                                           COLUMN_TASK_NUMBER + " INTEGER"               +
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

    public  static final String ANSWERS_TABLE_NAME   = "answers";
    private static final String ANSWERS_TABLE_CREATE = "CREATE TABLE " + ANSWERS_TABLE_NAME + " " +
                                                       "(" +
                                                           COLUMN_ID          + " INTEGER PRIMARY KEY, " +
                                                           COLUMN_RESULT_ID   + " INTEGER, "             +
                                                           COLUMN_TASK_NUMBER + " INTEGER, "             +
                                                           COLUMN_ANSWER      + " TEXT, "                +
                                                           COLUMN_CORRECT     + " INTEGER"               +
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
        db.execSQL(TASKS_TABLE_CREATE);
        db.execSQL(RESULTS_TABLE_CREATE);
        db.execSQL(ANSWERS_TABLE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        // Nothing
    }

    public Cursor getUsersList(SQLiteDatabase aDb)
    {
        Cursor aCursor=null;

        try
        {
            aCursor=aDb.query(
                              USERS_TABLE_NAME,
                              USERS_COLUMNS,
                              null,
                              null,
                              null,
                              null,
                              null
                             );
        }
        catch (Exception e)
        {
            Log.e(TAG, "Problem occured while getUsersList", e);
        }

        return aCursor;
    }

    public boolean isUsersListEmpty()
    {
    	boolean res=true;
        SQLiteDatabase aDb=null;
        Cursor aCursor=null;

        try
        {
            aDb=getReadableDatabase();
            aCursor=getUsersList(aDb);
            res=(aCursor==null || aCursor.getCount()==0);
        }
        catch (Exception e)
        {
            Log.e(TAG, "Problem occured while isUsersListEmpty", e);
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

    public long getUserId(SQLiteDatabase aDb, String aUserName)
    {
        long res=-1;
        Cursor aCursor=null;

        try
        {
            String[] aSelectionArgs={aUserName};
            aCursor=aDb.query(
                              USERS_TABLE_NAME,
                              USERS_COLUMNS,
                              COLUMN_USER_NAME+"=?",
                              aSelectionArgs,
                              null,
                              null,
                              null
                             );

            if (aCursor!=null && aCursor.getCount()>0)
            {
                aCursor.moveToFirst();
                res=aCursor.getLong(aCursor.getColumnIndexOrThrow(COLUMN_ID));
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
                              USERS_TABLE_NAME,
                              USERS_COLUMNS,
                              COLUMN_USER_NAME+"=?",
                              aSelectionArgs,
                              null,
                              null,
                              null
                             );

            if (aCursor==null || aCursor.getCount()==0)
            {
                ContentValues aValues=new ContentValues();
                aValues.put(COLUMN_USER_NAME, aUserName);

                res=aDb.insertOrThrow(
                                      USERS_TABLE_NAME,
                                      null,
                                      aValues
                                     );
            }
            else
            {
                aCursor.moveToFirst();
                res=aCursor.getLong(aCursor.getColumnIndexOrThrow(COLUMN_ID));
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

    public long getLessonId(SQLiteDatabase aDb, String aLessonName)
    {
        long res=-1;
        Cursor aCursor=null;

        try
        {
            String[] aSelectionArgs={aLessonName};
            aCursor=aDb.query(
                              LESSONS_TABLE_NAME,
                              LESSONS_COLUMNS,
                              COLUMN_LESSON_NAME+"=?",
                              aSelectionArgs,
                              null,
                              null,
                              null
                             );

            if (aCursor!=null && aCursor.getCount()>0)
            {
                aCursor.moveToFirst();
                res=aCursor.getLong(aCursor.getColumnIndexOrThrow(COLUMN_ID));
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

    public long getLessonId(String aLessonName)
    {
        long res=-1;
        SQLiteDatabase aDb=null;

        try
        {
            aDb=getReadableDatabase();
            res=getLessonId(aDb, aLessonName);
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

    public long getOrCreateLessonId(String aLessonName)
    {
        long res=-1;
        SQLiteDatabase aDb=null;
        Cursor aCursor=null;

        try
        {
            aDb=getWritableDatabase();

            String[] aSelectionArgs={aLessonName};
            aCursor=aDb.query(
                              LESSONS_TABLE_NAME,
                              LESSONS_COLUMNS,
                              COLUMN_LESSON_NAME+"=?",
                              aSelectionArgs,
                              null,
                              null,
                              null
                             );

            if (aCursor==null || aCursor.getCount()==0)
            {
                ContentValues aValues=new ContentValues();
                aValues.put(COLUMN_LESSON_NAME, aLessonName);

                res=aDb.insertOrThrow(
                                      LESSONS_TABLE_NAME,
                                      null,
                                      aValues
                                     );
            }
            else
            {
                aCursor.moveToFirst();
                res=aCursor.getLong(aCursor.getColumnIndexOrThrow(COLUMN_ID));
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

    public Cursor getTasksList(SQLiteDatabase aDb, long aUsedId, long aLessonId)
    {
        Cursor aCursor=null;

        try
        {
            String[] aSelectionArgs={
                                     String.valueOf(aUsedId),
                                     String.valueOf(aLessonId)
                                    };
            aCursor=aDb.query(
                              TASKS_TABLE_NAME,
                              TASKS_COLUMNS,
                              COLUMN_USER_ID     + "=?" + " AND " +
                              COLUMN_LESSON_ID   + "=?",
                              aSelectionArgs,
                              null,
                              null,
                              null
                             );
        }
        catch (Exception e)
        {
            Log.e(TAG, "Problem occured while getTasksList", e);
        }

        return aCursor;
    }

    public void setTaskFinished(long aUsedId, long aLessonId, int aTaskNumber)
    {
        SQLiteDatabase aDb=null;
        Cursor aCursor=null;

        try
        {
            aDb=getWritableDatabase();

            String[] aSelectionArgs={
                                     String.valueOf(aUsedId),
                                     String.valueOf(aLessonId),
                                     String.valueOf(aTaskNumber)
                                    };
            aCursor=aDb.query(
                              TASKS_TABLE_NAME,
                              TASKS_COLUMNS,
                              COLUMN_USER_ID     + "=?" + " AND " +
                              COLUMN_LESSON_ID   + "=?" + " AND " +
                              COLUMN_TASK_NUMBER + "=?",
                              aSelectionArgs,
                              null,
                              null,
                              null
                             );

            if (aCursor==null || aCursor.getCount()==0)
            {
                ContentValues aValues=new ContentValues();
                aValues.put(COLUMN_USER_ID,     aUsedId);
                aValues.put(COLUMN_LESSON_ID,   aLessonId);
                aValues.put(COLUMN_TASK_NUMBER, aTaskNumber);

                aDb.insertOrThrow(
                                  TASKS_TABLE_NAME,
                                  null,
                                  aValues
                                 );
            }
        }
        catch (Exception e)
        {
            Log.e(TAG, "Problem occured while setTaskFinished", e);
        }

        if (aCursor!=null)
        {
            aCursor.close();
        }

        if (aDb!=null)
        {
            aDb.close();
        }
    }

    public Cursor getResults(SQLiteDatabase aDb, String aUserName, String aLessonName)
    {
    	long aUserId=getUserId(aDb, aUserName);
        long aLessonId=getLessonId(aDb, aLessonName);

        Cursor res=null;

        try
        {
            String aSql="SELECT " + RESULTS_TABLE_NAME + "." + COLUMN_ID + ", " +
                                    COLUMN_USER_NAME + ", " +
                                    COLUMN_TIME + ", " +
                                    COLUMN_PERCENT + " " +
                        "FROM " + RESULTS_TABLE_NAME + " " +
                        "INNER JOIN " + USERS_TABLE_NAME +
                                    " ON " + COLUMN_USER_ID +
                                             "=" +
                                             USERS_TABLE_NAME + "." + COLUMN_ID + " " +
                        "WHERE " + COLUMN_USER_ID + "=?" + " " +
                        "AND " +   COLUMN_LESSON_ID + "=?";

            String[] aSelectionArgs={
            		                 String.valueOf(aUserId),
            		                 String.valueOf(aLessonId)
            		                };

            res=aDb.rawQuery(aSql, aSelectionArgs);
        }
        catch (Exception e)
        {
            Log.e(TAG, "Problem occured while getResults", e);
        }

        return res;
    }

    public Cursor getAnswers(SQLiteDatabase aDb, long aResultId)
    {
        Cursor res=null;

        try
        {
            String[] aSelectionArgs={String.valueOf(aResultId)};
            res=aDb.query(
                          ANSWERS_TABLE_NAME,
                          ANSWERS_COLUMNS,
                          COLUMN_RESULT_ID+"=?",
                          aSelectionArgs,
                          null,
                          null,
                          null
                         );
        }
        catch (Exception e)
        {
            Log.e(TAG, "Problem occured while getAnswers", e);
        }

        return res;
    }

    public void updateTasksStatus(String aUserName, String aLessonName, ArrayList<Task> aTasksList)
    {
        SQLiteDatabase aDb=null;
        Cursor aCursor=null;

        try
        {
            aDb=getReadableDatabase();

            long aUserId=getUserId(aDb, aUserName);
            long aLessonId=getLessonId(aDb, aLessonName);

            aCursor=getTasksList(aDb, aUserId, aLessonId);

            if (aCursor!=null)
            {
                int aTaskNumberIndex=aCursor.getColumnIndexOrThrow(COLUMN_TASK_NUMBER);

                while (aCursor.moveToNext())
                {
                    aTasksList.get(aCursor.getInt(aTaskNumberIndex)).setFinished(true);
                }
            }
        }
        catch (Exception e)
        {
            Log.e(TAG, "Impossible to set finished status for tasks", e);
        }

        if (aCursor!=null)
        {
            aCursor.close();
        }

        if (aDb!=null)
        {
            aDb.close();
        }
    }

    public ArrayList<Task> getTasksForResult(long aResultId, ArrayList<Task> aGlobalTasks)
    {
        ArrayList<Task> res=new ArrayList<Task>();

        SQLiteDatabase aDb=null;
        Cursor aCursor=null;

        try
        {
            aDb=getReadableDatabase();
            aCursor=getAnswers(aDb, aResultId);

            if (aCursor!=null)
            {
                int aTaskNumberIndex = aCursor.getColumnIndexOrThrow(COLUMN_TASK_NUMBER);
                int aAnswerIndex     = aCursor.getColumnIndexOrThrow(COLUMN_ANSWER);
                int aCorrectIndex    = aCursor.getColumnIndexOrThrow(COLUMN_CORRECT);

                while (aCursor.moveToNext())
                {
                    Task aTask=aGlobalTasks.get(aCursor.getInt(aTaskNumberIndex));
                    aTask.setAnswer(aCursor.getString(aAnswerIndex));
                    aTask.setFinished(aCursor.getInt(aCorrectIndex)!=0);
                    res.add(aTask);
                }
            }
        }
        catch (Exception e)
        {
            Log.e(TAG, "Impossible to take tasks from result № "+String.valueOf(aResultId), e);
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

    public long getTime(long aResultId)
    {
        long res=0;

        SQLiteDatabase aDb=null;
        Cursor aCursor=null;

        try
        {
            aDb=getReadableDatabase();
            String[] aSelectionArgs={String.valueOf(aResultId)};
            aCursor=aDb.query(
                              RESULTS_TABLE_NAME,
                              RESULTS_COLUMNS,
                              COLUMN_ID+"=?",
                              aSelectionArgs,
                              null,
                              null,
                              null
                             );

            if (aCursor!=null && aCursor.getCount()>0)
            {
                aCursor.moveToFirst();
                res=aCursor.getLong(aCursor.getColumnIndexOrThrow(COLUMN_TIME));
            }
        }
        catch (Exception e)
        {
            Log.e(TAG, "Impossible to take time from result № "+String.valueOf(aResultId), e);
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

    public int getPercent(long aResultId)
    {
        int res=0;

        SQLiteDatabase aDb=null;
        Cursor aCursor=null;

        try
        {
            aDb=getReadableDatabase();
            String[] aSelectionArgs={String.valueOf(aResultId)};
            aCursor=aDb.query(
                              RESULTS_TABLE_NAME,
                              RESULTS_COLUMNS,
                              COLUMN_ID+"=?",
                              aSelectionArgs,
                              null,
                              null,
                              null
                             );

            if (aCursor!=null && aCursor.getCount()>0)
            {
                aCursor.moveToFirst();
                res=aCursor.getInt(aCursor.getColumnIndexOrThrow(COLUMN_PERCENT));
            }
        }
        catch (Exception e)
        {
            Log.e(TAG, "Impossible to take percent from result № "+String.valueOf(aResultId), e);
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