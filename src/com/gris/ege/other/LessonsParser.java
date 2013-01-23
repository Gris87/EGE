package com.gris.ege.other;

import java.io.IOException;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import com.gris.ege.R;

import android.content.Context;

public class LessonsParser
{
    private static final String TAG="LessonsParser";



    public void parse(Context aContext)
    {
        try
        {
            XmlPullParser aParser = aContext.getResources().getXml(R.xml.lessons);

            aParser.next();
            aParser.nextTag();

            readLessons(aParser);
        }
        catch (Exception e)
        {
            Log.e(TAG, "Error during xml parsing", e);
        }
    }

    private void readLessons(XmlPullParser aParser) throws XmlPullParserException, IOException
    {
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
                readLesson(aParser);
            }
            else
            {
                skip(aParser);
            }
        }
    }

    private void readLesson(XmlPullParser aParser) throws XmlPullParserException, IOException
    {
        aParser.require(XmlPullParser.START_TAG, null, "lesson");

        String aId      = aParser.getAttributeValue(null, "id");
        String aName    = readText(aParser);

        aParser.require(XmlPullParser.END_TAG, null, "lesson");

        for (int i=0; i<GlobalData.lessons.size(); ++i)
        {
            if (GlobalData.lessons.get(i).getId().equals(aId))
            {
                GlobalData.lessons.get(i).setName(aName);
                break;
            }
        }
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
