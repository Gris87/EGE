package com.gris.ege.other;

import java.io.IOException;
import java.util.ArrayList;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import com.gris.ege.R;

import android.content.Context;
import android.util.Log;

public class LessonsParser
{
    private static final String TAG="LessonsParser";



    public ArrayList<Lesson> parse(Context aContext)
    {
        ArrayList<Lesson> res=null;

        try
        {
            XmlPullParser aParser = aContext.getResources().getXml(R.xml.lessons);

            aParser.next();
            aParser.nextTag();

            res=readLessons(aParser);
        }
        catch (Exception e)
        {
            Log.e(TAG, "Error during xml parsing", e);
        }

        return res;
    }

    private ArrayList<Lesson> readLessons(XmlPullParser aParser) throws XmlPullParserException, IOException
    {
        ArrayList<Lesson> res=new ArrayList<Lesson>();

        aParser.require(XmlPullParser.START_TAG, null, "lessons");

        while (aParser.next()!=XmlPullParser.END_TAG)
        {
            if (aParser.getEventType()!=XmlPullParser.START_TAG)
            {
                continue;
            }

            String aTagName = aParser.getName();

            if (aTagName.equals("lesson"))
            {
                res.add(readLesson(aParser));
            }
            else
            {
                skip(aParser);
            }
        }

        return res;
    }

    private Lesson readLesson(XmlPullParser aParser) throws XmlPullParserException, IOException
    {
        aParser.require(XmlPullParser.START_TAG, null, "lesson");

        String aId      = aParser.getAttributeValue(null, "id");
        String aTimeStr = aParser.getAttributeValue(null, "time");
        String aName    = readText(aParser);

        aParser.require(XmlPullParser.END_TAG, null, "lesson");

        int aTime=Integer.parseInt(aTimeStr);

        return new Lesson(aId, aName, aTime);
    }

    private String readText(XmlPullParser aParser) throws IOException, XmlPullParserException
    {
        String res="";

        if (aParser.next()==XmlPullParser.TEXT)
        {
            res=aParser.getText();
            aParser.nextTag();
        }

        return res;
    }

    private void skip(XmlPullParser aParser) throws XmlPullParserException, IOException
    {
        if (aParser.getEventType()!=XmlPullParser.START_TAG)
        {
            throw new IllegalStateException();
        }

        int aDepth=1;

        while (aDepth!=0)
        {
            switch (aParser.next())
            {
                case XmlPullParser.END_TAG:
                    aDepth--;
                break;
                case XmlPullParser.START_TAG:
                    aDepth++;
                break;
            }
        }
    }
}
