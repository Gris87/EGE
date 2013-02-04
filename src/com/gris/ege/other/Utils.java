package com.gris.ege.other;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.preference.PreferenceManager;

public class Utils
{
    public static String timeToString(String aFormat, long aTime)
    {
        aTime/=1000;

        int aSeconds=(int)(aTime % 60);

        aTime=(aTime-aSeconds) / 60;

        int aMinutes=(int)(aTime % 60);

        aTime=(aTime-aMinutes) / 60;

        int aHours=(int)aTime;

        String aHoursStr=String.valueOf(aHours);
        String aMinutesStr=String.valueOf(aMinutes);
        String aSecondsStr=String.valueOf(aSeconds);

        if (aMinutes<10)
        {
            aMinutesStr="0"+aMinutesStr;
        }

        if (aSeconds<10)
        {
            aSecondsStr="0"+aSecondsStr;
        }

        return String.format(aFormat, aHoursStr, aMinutesStr, aSecondsStr);
    }

    public static boolean checkWifiOrNet(Context aContext)
    {
        SharedPreferences aPreferences=PreferenceManager.getDefaultSharedPreferences(aContext);

        if (!aPreferences.getBoolean(GlobalData.OPTION_WIFI_ONLY, false))
        {
            return true;
        }

        ConnectivityManager aManager=(ConnectivityManager) aContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo aNetworkInfo=aManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        return aNetworkInfo.isConnected();
    }
}
