package com.example.countertask.view;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.example.countertask.model.AsyncTaskCounter;
import com.example.countertask.model.ThreadedCounter;

public class MainActivityModel extends AndroidViewModel
{
    private boolean toggle;
    private String error;
    private int start_count;
    private int radioButtonId;

    private ThreadedCounter threadedCounter;
    private AsyncTaskCounter asyncTaskCounter;

    public MainActivityModel(@NonNull Application application)
    {
        super(application);
        this.toggle = false;
        this.error = null;
        this.start_count = 0;
        this.radioButtonId = 0;
    }

    public void setRadioButtonId(int radioButtonId)
    {
        this.radioButtonId = radioButtonId;
    }

    public int getRadioButtonId()
    {
        return this.radioButtonId;
    }

    public boolean isToggle()
    {
        return toggle;
    }

    public String getError()
    {
        return error;
    }

    public int getStartCount()
    {
        return start_count;
    }

    public ThreadedCounter getThreadedCounter()
    {
        return threadedCounter;
    }

    public void setToggle(boolean toggle)
    {
        this.toggle = toggle;
    }

    public void setError(String error)
    {
        this.error = error;
    }

    public void setStartCount(int start_count)
    {
        this.start_count = start_count;
    }

    public void setThreadedCounter(ThreadedCounter threadedCounter)
    {
        this.threadedCounter = threadedCounter;
    }

    public AsyncTaskCounter getAsyncTaskCounter()
    {
        return asyncTaskCounter;
    }

    public void setAsyncTaskCounter(AsyncTaskCounter asyncTaskCounter)
    {
        this.asyncTaskCounter = asyncTaskCounter;
    }
}
