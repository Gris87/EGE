package com.gris.ege.other;

public class Lesson
{
    private String mId;
    private String mName;
    private int    mTime;



    public Lesson()
    {
        mId="";
        mName="";
        mTime=0;
    }

    public Lesson(String aId, String aName, int aTime)
    {
        mId=aId;
        mName=aName;
        mTime=aTime;
    }

    public String getId()
    {
        return mId;
    }

    public String getName()
    {
        return mName;
    }

    public int getTime()
    {
        return mTime;
    }
}
