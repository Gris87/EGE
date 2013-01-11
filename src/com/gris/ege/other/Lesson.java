package com.gris.ege.other;

public class Lesson
{
    private String mId;
    private String mName;

    public Lesson()
    {
        mId="";
        mName="";
    }

    public Lesson(String aId, String aName)
    {
        mId=aId;
        mName=aName;
    }

    public String getId()
    {
        return mId;
    }

    public String getName()
    {
        return mName;
    }
}
