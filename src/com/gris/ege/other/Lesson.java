package com.gris.ege.other;

public class Lesson
{
    private String mId;
    private String mName;
    private short  mTime;
    private byte   mScoreA;
    private byte   mScoreB;
    private byte   mScoreC;



    public Lesson()
    {
        mId="";
        mName="";
        mTime=0;
        mScoreA=0;
        mScoreB=0;
        mScoreC=0;
    }

    public Lesson(String aId, short aTime, byte aScoreA, byte aScoreB, byte aScoreC)
    {
        mId=aId;
        mName="";
        mTime=aTime;
        mScoreA=aScoreA;
        mScoreB=aScoreB;
        mScoreC=aScoreC;
    }

    public String getId()
    {
        return mId;
    }

    public String getName()
    {
        return mName;
    }

    public void setName(String aName)
    {
        mName=aName;
    }

    public short getTime()
    {
        return mTime;
    }

    public byte getScoreA()
    {
        return mScoreA;
    }

    public byte getScoreB()
    {
        return mScoreB;
    }

    public byte getScoreC()
    {
        return mScoreC;
    }
}
