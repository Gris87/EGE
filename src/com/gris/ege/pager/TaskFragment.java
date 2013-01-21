package com.gris.ege.pager;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import com.gris.ege.R;
import com.gris.ege.other.GlobalData;
import com.gris.ege.other.Task;

import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ViewAnimator;

public class TaskFragment extends Fragment implements OnClickListener
{
    private static final String TAG="TaskFragment";



    public  static final int MODE_VIEW_TASK   = 0;
    public  static final int MODE_TEST_TASK   = 1;
    public  static final int MODE_VIEW_RESULT = 2;

    private static final int PAGE_DOWNLOAD    = 0;
    private static final int PAGE_RETRY       = 1;
    private static final int PAGE_IMAGE       = 2;



    private TextView       mTaskHeaderView;
    private TextView       mTaskStatusView;
    private ViewAnimator   mTaskViewAnimator;
    private ProgressBar    mDownloadProgressBar;
    private Button         mRetryButton;
    private ImageView      mTaskImageView;
    private TextView       mAnswerTextView;
    private RelativeLayout mBottomLayout;
    private EditText       mAnswerEditText;
    private Button         mAnswerButton;

    private Task mTask;
    private int  mMode;



    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        Bundle aArgs=getArguments();

        if (aArgs!=null)
        {
            int aTaskId=aArgs.getInt(GlobalData.TASK_ID);

            mTask=GlobalData.tasks.get(aTaskId);
            mMode=aArgs.getInt(GlobalData.MODE);
        }
        else
        {
            mTask=GlobalData.tasks.get(0);
            mMode=MODE_VIEW_TASK;
        }
    }

    @Override
    public View onCreateView(LayoutInflater aInflater, ViewGroup aContainer, Bundle aSavedInstanceState)
    {
        View aView=aInflater.inflate(R.layout.task_page_item, aContainer, false);

        // Get controls
        mTaskHeaderView      = (TextView)      aView.findViewById(R.id.taskHeaderTextView);
        mTaskStatusView      = (TextView)      aView.findViewById(R.id.taskStatusTextView);
        mTaskViewAnimator    = (ViewAnimator)  aView.findViewById(R.id.taskViewAnimator);
        mDownloadProgressBar = (ProgressBar)   aView.findViewById(R.id.downloadProgressBar);
        mRetryButton         = (Button)        aView.findViewById(R.id.retryButton);
        mTaskImageView       = (ImageView)     aView.findViewById(R.id.taskImageView);
        mAnswerTextView      = (TextView)      aView.findViewById(R.id.answerTextView);
        mBottomLayout        = (RelativeLayout)aView.findViewById(R.id.bottomLayout);
        mAnswerEditText      = (EditText)      aView.findViewById(R.id.answerEditText);
        mAnswerButton        = (Button)        aView.findViewById(R.id.answerButton);

        // Set listeners
        mRetryButton.setOnClickListener(this);
        mAnswerButton.setOnClickListener(this);

        // Initialize controls
        mTaskHeaderView.setText(getString(R.string.task_header, mTask.getCategory(), mTask.getId()+1));
        updateStatus();

        switch (mMode)
        {
            case MODE_VIEW_TASK:
                mAnswerTextView.setVisibility(View.GONE);
            break;
            case MODE_TEST_TASK:
                mAnswerTextView.setVisibility(View.GONE);
                mAnswerButton.setVisibility(View.GONE);
            break;
            case MODE_VIEW_RESULT:
                mBottomLayout.setVisibility(View.GONE);
            break;
        }

        downloadImage();

        return aView;
    }

    public void updateStatus()
    {
        if (mMode==MODE_TEST_TASK)
        {
            mTaskStatusView.setVisibility(View.GONE);
        }
        else
        {
            mTaskStatusView.setVisibility(View.VISIBLE);

            if (mTask.isFinished())
            {
                mTaskStatusView.setText(getString(mMode==MODE_VIEW_TASK? R.string.finished : R.string.correct));
                mTaskStatusView.setTextColor(getResources().getColor(R.color.good));
            }
            else
            {
                mTaskStatusView.setText(getString(mMode==MODE_VIEW_TASK? R.string.not_finished : R.string.not_correct));
                mTaskStatusView.setTextColor(getResources().getColor(R.color.bad));
            }
        }
    }

    public void downloadImage()
    {
        new DownloadImageTask().execute();
    }

    public void checkAnswer()
    {

    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.retryButton:
                downloadImage();
            break;
            case R.id.answerButton:
                checkAnswer();
            break;
        }
    }

    private class DownloadImageTask extends AsyncTask<Void, Void, Drawable>
    {
        @Override
        protected void onPreExecute()
        {
            mTaskViewAnimator.setDisplayedChild(PAGE_DOWNLOAD);
        }

        @Override
        protected Drawable doInBackground(Void... aNothing)
        {
            Drawable res=null;

            try
            {
                res=getImage();
            }
            catch (Exception e)
            {
                Log.w(TAG, "Problem while loading image", e);
            }

            return res;
        }


        private Drawable getImage() throws IOException
        {
            String aFileName=GlobalData.selectedLesson.getId()+"/"+String.valueOf(mTask.getId()+1)+".png";

            if (new File(GlobalData.PATH_ON_SD_CARD+aFileName).exists())
            {
                Drawable aDrawable=Drawable.createFromPath(GlobalData.PATH_ON_SD_CARD+aFileName);

                if (aDrawable!=null)
                {
                    return aDrawable;
                }
            }

            // Download file
            URL aUrl=new URL(GlobalData.PATH_ON_NET+aFileName);

            HttpURLConnection aConnection=(HttpURLConnection)aUrl.openConnection();
            aConnection.setReadTimeout(10000);
            aConnection.setConnectTimeout(15000);
            aConnection.setRequestMethod("GET");
            aConnection.setDoInput(true);

            // Starts the query
            aConnection.connect();
            InputStream in=aConnection.getInputStream();

            InputStream res=in;

            try
            {
                byte[] aBuffer=new byte[4096];

                new File(GlobalData.PATH_ON_SD_CARD+GlobalData.selectedLesson.getId()).mkdirs();
                new File(GlobalData.PATH_ON_SD_CARD+".nomedia").createNewFile();

                FileOutputStream aNewFile=new FileOutputStream(GlobalData.PATH_ON_SD_CARD+aFileName);

                while (in.available()>0)
                {
                    int aBytes=in.read(aBuffer);
                    aNewFile.write(aBuffer, 0, aBytes);
                }

                aNewFile.close();
                in.close();

                res=new FileInputStream(GlobalData.PATH_ON_SD_CARD+aFileName);
            }
            catch (Exception e)
            {
                Log.w(TAG, "Problem while saving image on sd card", e);
            }

            Drawable aDrawable=Drawable.createFromStream(res, null);
            res.close();

            return aDrawable;
        }

        @Override
        protected void onPostExecute(Drawable aResult)
        {
            if (aResult!=null)
            {
                mTaskImageView.setImageDrawable(aResult);
                mTaskViewAnimator.setDisplayedChild(PAGE_IMAGE);
            }
            else
            {
                mTaskViewAnimator.setDisplayedChild(PAGE_RETRY);
            }
        }
    }
}
