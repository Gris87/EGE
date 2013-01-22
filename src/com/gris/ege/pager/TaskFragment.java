package com.gris.ege.pager;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import com.gris.ege.R;
import com.gris.ege.db.ResultsOpenHelper;
import com.gris.ege.other.GlobalData;
import com.gris.ege.other.Task;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
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
    private Button         mRetryButton;
    private ImageView      mTaskImageView;
    private TextView       mAnswerTextView;
    private RelativeLayout mBottomLayout;
    private EditText       mAnswerEditText;
    private Button         mAnswerButton;

    private Task mTask;
    private int  mMode;
    private long mUserId;
    private long mLessonId;



    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        Bundle aArgs=getArguments();

        if (aArgs!=null)
        {
            mTask     = GlobalData.tasks.get(aArgs.getInt( GlobalData.TASK_ID));
            mMode     =                      aArgs.getInt( GlobalData.MODE);
            mUserId   =                      aArgs.getLong(GlobalData.USER_ID);
            mLessonId =                      aArgs.getLong(GlobalData.LESSON_ID);
        }
        else
        {
            mTask     = GlobalData.tasks.get(0);
            mMode     = MODE_VIEW_TASK;
            mUserId   = 0;
            mLessonId = 0;
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
                mAnswerTextView.setText(getString(R.string.answer, mTask.getAnswer()));
            break;
        }

        downloadImage();

        if (mTask.getCategory().charAt(0)=='A')
        {
            mAnswerEditText.setRawInputType(
                                            InputType.TYPE_CLASS_NUMBER
                                            |
                                            InputType.TYPE_NUMBER_FLAG_SIGNED
                                            |
                                            InputType.TYPE_NUMBER_FLAG_DECIMAL
                                           );

            mAnswerEditText.setSingleLine(true);
        }
        else
        if (mTask.getCategory().charAt(0)=='B')
        {
            mAnswerEditText.setRawInputType(
                                            InputType.TYPE_CLASS_NUMBER
                                            |
                                            InputType.TYPE_NUMBER_FLAG_SIGNED
                                            |
                                            InputType.TYPE_NUMBER_FLAG_DECIMAL
                                           );

            mAnswerEditText.setSingleLine(true);
        }
        else
        if (mTask.getCategory().charAt(0)=='C')
        {
            mAnswerEditText.setRawInputType(
                                            InputType.TYPE_CLASS_TEXT
                                            |
                                            InputType.TYPE_TEXT_VARIATION_NORMAL
                                            |
                                            InputType.TYPE_TEXT_FLAG_MULTI_LINE
                                           );

            mAnswerEditText.setSingleLine(false);
        }
        else
        {
            Log.e(TAG, "Invalid category \""+mTask.getCategory()+"\" for task № "+String.valueOf(mTask.getId()));
        }

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

    public void checkAnswer(boolean aShowToast, boolean aCorrect)
    {
        if (aShowToast)
        {
            Toast.makeText(getActivity(), aCorrect? R.string.correct : R.string.not_correct, Toast.LENGTH_SHORT).show();
        }

        if (aCorrect)
        {
            new ResultsOpenHelper(getActivity()).setTaskFinished(mUserId, mLessonId, mTask.getId());
            mTask.setFinished(true);
            updateStatus();

            if (aShowToast)
            {
                InputMethodManager imm=(InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(mAnswerEditText.getWindowToken(), 0);
            }
        }
    }

    public void checkAnswer(final boolean aShowToast)
    {
        if (mTask.getCategory().charAt(0)=='A')
        {
            checkAnswer(aShowToast, mTask.getAnswer().equalsIgnoreCase(getAnswer()));
        }
        else
        if (mTask.getCategory().charAt(0)=='B')
        {
            checkAnswer(aShowToast, mTask.getAnswer().equalsIgnoreCase(getAnswer()));
        }
        else
        if (mTask.getCategory().charAt(0)=='C')
        {
            DialogFragment aCheckDialog = new DialogFragment()
            {
                private boolean mShowToast=aShowToast;

                public Dialog onCreateDialog(Bundle savedInstanceState)
                {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

                    builder.setMessage(getString(R.string.is_it_correct, mTask.getAnswer()))
                           .setPositiveButton(R.string.correct, new DialogInterface.OnClickListener()
                           {
                               public void onClick(DialogInterface dialog, int id)
                               {
                                   checkAnswer(mShowToast, true);
                               }
                           })
                           .setNegativeButton(R.string.not_correct, new DialogInterface.OnClickListener()
                           {
                               public void onClick(DialogInterface dialog, int id)
                               {
                                   checkAnswer(mShowToast, false);
                               }
                           });

                    return builder.create();
                }
            };

            aCheckDialog.show(getFragmentManager(), "CheckDialog");
        }
        else
        {
            Log.e(TAG, "Invalid category \""+mTask.getCategory()+"\" for task № "+String.valueOf(mTask.getId()));
        }
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
                checkAnswer(true);
            break;
        }
    }

    public String getAnswer()
    {
        return mAnswerEditText.getText().toString();
    }

    public Task getTask()
    {
        return mTask;
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
                else
                {
                    Log.w(TAG, "Invalid file on sdcard: "+GlobalData.PATH_ON_SD_CARD+aFileName);
                }
            }

            // Download file
            URL aUrl=new URL(GlobalData.PATH_ON_NET+aFileName);

            HttpURLConnection aConnection=(HttpURLConnection)aUrl.openConnection();
            aConnection.setReadTimeout(10000);
            aConnection.setConnectTimeout(15000);
            aConnection.setRequestMethod("GET");
            aConnection.setDoInput(true);

            // Download file
            aConnection.connect();
            InputStream in=aConnection.getInputStream();

            InputStream res=in;

            try
            {
                byte[] aBuffer=new byte[4096];

                new File(GlobalData.PATH_ON_SD_CARD+GlobalData.selectedLesson.getId()).mkdirs();
                new File(GlobalData.PATH_ON_SD_CARD+".nomedia").createNewFile();

                FileOutputStream aNewFile=new FileOutputStream(GlobalData.PATH_ON_SD_CARD+aFileName);

                do
                {
                    int aBytes=in.read(aBuffer);

                    if (aBytes<=0)
                    {
                        break;
                    }

                    aNewFile.write(aBuffer, 0, aBytes);
                } while(true);

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

            if (aDrawable!=null)
            {
                return aDrawable;
            }
            else
            {
                Log.w(TAG, "Invalid file on sdcard after downloading: "+GlobalData.PATH_ON_SD_CARD+aFileName);
            }

            return null;
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
