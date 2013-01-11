package com.gris.ege;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.content.Context;
import android.util.Log;
import android.util.Xml;

public class LessonsParser
{
    private static final String TAG="LessonsParser";

    public ArrayList<Lesson> parse(Context context)
    {
        // Open XML file
        InputStream in = context.getResources().openRawResource(R.xml.lessons);

        byte[] aArray=new byte[200];

        try
        {
            in.read(aArray);
        } catch (IOException e)
        {
            Log.e(TAG, "Error during xml parsing", e);
        }

        for (int i=0; i<aArray.length; ++i)
        {
            Log.e(TAG, String.valueOf(aArray[i]));
        }



        // Parsing XML file
        ArrayList<Lesson> res=null;

        try
        {
            XmlPullParser aParser = Xml.newPullParser();

            aParser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            aParser.setInput(in, null);
            aParser.nextTag();

            res=readLessons(aParser);
        }
        catch (Exception e)
        {
            Log.e(TAG, "Error during xml parsing", e);
        }



        // Close XML file
        try
        {
            in.close();
        }
        catch (IOException e)
        {
            Log.w(TAG, "Impossible to close file", e);
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
        String aId = aParser.getAttributeValue(null, "id");
        String aName = readText(aParser);
        aParser.require(XmlPullParser.END_TAG, null, "lesson");

        return new Lesson(aId, aName);
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
