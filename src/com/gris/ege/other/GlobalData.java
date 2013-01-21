package com.gris.ege.other;

import java.util.ArrayList;

public class GlobalData
{
    public static final String PREFS_NAME             = "Settings";

    public static final String OPTION_USER_NAME       = "userName";
    public static final String OPTION_SELECTED_LESSON = "selectedLesson";

    public static final String USER_NAME              = "userName";
    public static final String LESSON_ID              = "lessonId";
    public static final String TASK_ID                = "taskId";
    public static final String RESULT_ID              = "resultId";
    public static final String TASKS_COUNT            = "tasksCount";
    public static final String MODE                   = "mode";

    public static final String PATH_ON_SD_CARD        = "/EGE/";



    public static ArrayList<Lesson> lessons;
    public static ArrayList<Task>   tasks;      // current opened tasks (full list for selectedLesson)

    public static Lesson            selectedLesson;

    static
    {
        lessons=new ArrayList<Lesson>();

        lessons.add(new Lesson("mathematics", (short)240, (byte)0, (byte)1, (byte)2));
        lessons.add(new Lesson("physics",     (short)240, (byte)1, (byte)2, (byte)3));
    }
}
