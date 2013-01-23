package com.gris.ege.other;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Log
{
	private static final String TAG="Log";

	private static final String APP_NAME  = "EGE";
	private static final String FILE_PATH = GlobalData.PATH_ON_SD_CARD+"Logs";

	private static final boolean DEBUG          = true;
	private static final boolean OUTPUT_TO_FILE = true;
	private static final boolean ONLY_APP_TAG   = true;



	private static String aFileName="";



	private static void writeToFile(String aLevel, String aTag, String aMessage, Throwable aException)
	{
		if (OUTPUT_TO_FILE)
		{
			try
            {
				String aCurTimeStr=new SimpleDateFormat("MM-DD kk:mm:ss:SSS", new Locale("en")).format(new Date());
				String aPrefixText=aCurTimeStr+": "+aLevel+"/"+APP_NAME+"(9999)"+": ";

				if (aFileName.equals(""))
				{
					new File(FILE_PATH).mkdirs();

					aFileName=FILE_PATH+"/test.txt";
				}

	            FileOutputStream aFileStream = new FileOutputStream(aFileName, true);
	            PrintWriter aPrinter         = new PrintWriter(aFileStream, true);

	            aPrinter.println(aPrefixText+aMessage);

	            aPrinter.close();
            }
            catch (Exception e)
            {
            	android.util.Log.e(TAG, "Impossible write log to SD card", e);
            }
		}
	}

	public static void v(String aTag, String aMsg)
	{
		v(aTag, aMsg, null);
	}

	public static void v(String aTag, String aMsg, Throwable tr)
	{
		if (ONLY_APP_TAG)
		{
			aMsg = aTag + ": " + aMsg;
			aTag = APP_NAME;
		}

		if (DEBUG)
		{
			android.util.Log.v(aTag, aMsg, tr);
		}

		writeToFile("VERBOSE", aTag, aMsg, tr);
	}

	public static void d(String aTag, String aMsg)
	{
		d(aTag, aMsg, null);
	}

	public static void d(String aTag, String aMsg, Throwable tr)
	{
		if (ONLY_APP_TAG)
		{
			aMsg = aTag + ": " + aMsg;
			aTag = APP_NAME;
		}

		if (DEBUG)
		{
			android.util.Log.d(aTag, aMsg, tr);
		}

		writeToFile("DEBUG", aTag, aMsg, tr);
	}

	public static void i(String aTag, String aMsg)
	{
		i(aTag, aMsg, null);
	}

	public static void i(String aTag, String aMsg, Throwable tr)
	{
		if (ONLY_APP_TAG)
		{
			aMsg = aTag + ": " + aMsg;
			aTag = APP_NAME;
		}

		if (DEBUG)
		{
			android.util.Log.i(aTag, aMsg, tr);
		}

		writeToFile("INFO", aTag, aMsg, tr);
	}

	public static void w(String aTag, String aMsg)
	{
		w(aTag, aMsg, null);
	}

	public static void w(String aTag, String aMsg, Throwable tr)
	{
		if (ONLY_APP_TAG)
		{
			aMsg = aTag + ": " + aMsg;
			aTag = APP_NAME;
		}

		if (DEBUG)
		{
			android.util.Log.w(aTag, aMsg, tr);
		}

		writeToFile("WARN", aTag, aMsg, tr);
	}

	public static void e(String aTag, String aMsg)
	{
		e(aTag, aMsg, null);
	}

	public static void e(String aTag, String aMsg, Throwable tr)
	{
		if (ONLY_APP_TAG)
		{
			aMsg = aTag + ": " + aMsg;
			aTag = APP_NAME;
		}

		if (DEBUG)
		{
			android.util.Log.e(aTag, aMsg, tr);
		}

		writeToFile("ERROR", aTag, aMsg, tr);
	}
}
