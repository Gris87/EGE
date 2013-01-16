package com.gris.ege.other;

import java.util.ArrayList;

public class GlobalData
{
    public static final String PREFS_NAME             = "Settings";

    public static final String OPTION_USER_NAME       = "userName";
    public static final String OPTION_SELECTED_LESSON = "selectedLesson";

    public static final String LESSON_ID              = "lessonId";
    public static final String TASK_ID                = "taskId";
    public static final String WITH_ANSWERS           = "withAnswers";

    public static final String PATH_ON_SD_CARD        = "/EGE/";



    public static ArrayList<Lesson> lessons;
    public static ArrayList<Task>   tasks;      // current opened tasks (full list for selectedLesson)

    public static String selectedLesson = "";
}
