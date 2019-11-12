package com.example.countertask.view;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.example.countertask.model.ThreadedCounter;

public class MainActivityModel extends AndroidViewModel
{
    private boolean toggle;
    private String error;
    private int start_count;

    private ThreadedCounter threadedCounter;

    public MainActivityModel(@NonNull Application application)
    {
        super(application);
        this.toggle = false;
        this.error = null;
        this.start_count = 0;
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
}
