package com.gris.ege.pager;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

public class TouchViewPager extends ViewPager
{
    private TaskFragment mCurrentPage;

    public TouchViewPager(Context context)
    {
        super(context);
    }

    public TouchViewPager(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }

    public boolean onInterceptTouchEvent(MotionEvent event)
    {
        if (mCurrentPage!=null)
        {
            this.requestDisallowInterceptTouchEvent(true);
        }
        else
        {
            this.requestDisallowInterceptTouchEvent(false);
        }

        return super.onInterceptTouchEvent(event);
    }

    public TaskFragment getCurrentPage()
    {
        return mCurrentPage;
    }

    public void setCurrentPage(TaskFragment aCurrentPage)
    {
        mCurrentPage=aCurrentPage;
    }
}
