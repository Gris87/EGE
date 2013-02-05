package com.gris.ege.other;

public class Task
{
    private int     mId;
    private String  mCategory;
    private String  mAnswer;
    private byte    mScore;
    private byte    mMaxScore;
    private int     mGroup;
    private boolean mWithMistakes;
    private boolean mSelfRating;



    public Task()
    {
        mId           = 0;
        mCategory     = "";
        mAnswer       = "";
        mScore        = 0;
        mMaxScore     = 0;
        mGroup        = 0;
        mWithMistakes = false;
        mSelfRating   = false;
    }

    public Task(int aId, String aCategory, String aAnswer, byte aMaxScore, int aGroup, boolean aWithMistakes, boolean aSelfRating)
    {
        mId           = aId;
        mCategory     = aCategory;
        mAnswer       = aAnswer;
        mScore        = 0;
        mMaxScore     = aMaxScore;
        mGroup        = aGroup;
        mWithMistakes = aWithMistakes;
        mSelfRating   = aSelfRating;
    }

    public int getId()
    {
        return mId;
    }

    public void setId(int aId)
    {
        mId=aId;
    }

    public String getCategory()
    {
        return mCategory;
    }

    public void setCategory(String aCategory)
    {
        mCategory=aCategory;
    }

    public String getAnswer()
    {
        return mAnswer;
    }

    public void setAnswer(String aAnswer)
    {
        mAnswer=aAnswer;
    }

    public byte getScore()
    {
        return mScore;
    }

    public void setScore(byte aScore)
    {
        if (aScore>mMaxScore)
        {
            mScore=mMaxScore;
        }
        else
        {
            mScore=aScore;
        }
    }

    public byte getMaxScore()
    {
        return mMaxScore;
    }

    public void setMaxScore(byte aMaxScore)
    {
        mMaxScore=aMaxScore;
    }

    public int getGroup()
    {
	    return mGroup;
    }

    public void setGroup(int aGroup)
    {
	    mGroup=aGroup;
    }

    public boolean isWithMistakes()
    {
        return mWithMistakes;
    }

    public void setWithMistakes(boolean aWithMistakes)
    {
        mWithMistakes=aWithMistakes;
    }

    public boolean isSelfRating()
    {
        return mSelfRating;
    }

    public void setSelfRating(boolean aSelfRating)
    {
        mSelfRating=aSelfRating;
    }

    public boolean isFinished()
    {
        return mScore>=mMaxScore;
    }

    public void setFinished(boolean aFinished)
    {
        if (aFinished)
        {
            mScore=mMaxScore;
        }
        else
        {
            mScore=0;
        }
    }
}
