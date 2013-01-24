package com.gris.ege.other;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Log
{
	private static final String  TAG            = "Log";

	private static final String  APP_NAME       = "EGE";
	private static final String  FILE_PATH      = GlobalData.PATH_ON_SD_CARD+"Logs";

	private static final boolean DEBUG          = true;
	private static final boolean OUTPUT_TO_FILE = true;
	private static final boolean ONLY_APP_TAG   = true;

	private static final int     REMOVE_IF_LESS = 500;



	private static String mFileName="";



	public static void reset()
	{
		mFileName="";
	}

	private static void writeToFile(String aLevel, String aTag, String aMessage, Throwable aException)
	{
		if (OUTPUT_TO_FILE)
		{
			try
            {
				String aCurTimeStr=new SimpleDateFormat("MM-DD kk:mm:ss:SSS", new Locale("en")).format(new Date());
				String aPrefixText=aCurTimeStr+": "+aLevel+"/"+APP_NAME+"(9999)"+": ";

				if (mFileName.equals(""))
				{
					new File(FILE_PATH).mkdirs();

					int aCurIndex=1;

					do
					{
						File aCurFile=new File(FILE_PATH+"/"+String.valueOf(aCurIndex)+".dlv");

						if (aCurFile.exists())
						{
							if (aCurFile.length()<REMOVE_IF_LESS)
							{
								aCurFile.delete();
								break;
							}
						}
						else
						{
							break;
						}

						++aCurIndex;
					} while(true);

					while (new File(FILE_PATH+"/"+String.valueOf(aCurIndex)+".dlv").exists())
					{
						aCurIndex++;
					}

					mFileName=FILE_PATH+"/"+String.valueOf(aCurIndex)+".dlv";
				}

	            FileOutputStream aFileStream = new FileOutputStream(mFileName, true);
	            PrintWriter aPrinter         = new PrintWriter(aFileStream, true);

	            aPrinter.println(aPrefixText+aMessage);



	            if (aException!=null)
	            {
	            	String aExceptionMsg=aException.getLocalizedMessage();

	            	if (aExceptionMsg!=null)
	            	{
	            		aPrinter.println(aPrefixText+aExceptionMsg);
	            	}



	            	StackTraceElement[] aStack=aException.getStackTrace();

	            	for (int i=0; i<aStack.length; ++i)
	            	{
	            		String aStackStr="\tat "+aStack[i].getClassName()+"."+aStack[i].getMethodName()+"(";
	            		int aLineNumber=aStack[i].getLineNumber();

	            		if (aLineNumber==-2)
	            		{
	            			aStackStr=aStackStr+"Native Method";
	            		}
	            		else
	            		if (aLineNumber<1)
	            		{
	            			aStackStr=aStackStr+"Unknown";
	            		}
	            		else
	            		{
	            			aStackStr=aStackStr+aStack[i].getFileName()+":"+String.valueOf(aLineNumber);
	            		}

	            		aStackStr=aStackStr+")";

	            		aPrinter.println(aPrefixText+aStackStr);
	            	}
	            }



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
