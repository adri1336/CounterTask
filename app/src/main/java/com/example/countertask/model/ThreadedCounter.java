package com.example.countertask.model;

import android.content.Context;
import android.os.Handler;
import android.widget.TextView;

public class ThreadedCounter extends Thread
{
    private int counter;
    private TextView tvCount;
    private Handler mainHandler;
    private OnCounterFinishedListener onCounterFinishedListener;
    private boolean active;

    public ThreadedCounter(int counter, TextView tvCount, Context context)
    {
        this.counter = counter;
        this.tvCount = tvCount;
        this.mainHandler = new Handler(context.getMainLooper());
        this.active = true;
    }

    public void setActive(boolean active)
    {
        this.active = active;
    }

    public void setTvCount(TextView tvCount)
    {
        this.tvCount = tvCount;
    }

    public void setContext(Context context)
    {
        this.mainHandler = new Handler(context.getMainLooper());
    }

    public void setOnCounterFinishedListener(OnCounterFinishedListener onCounterFinishedListener)
    {
        this.onCounterFinishedListener = onCounterFinishedListener;
    }

    public int getCounter()
    {
        return this.counter;
    }

    @Override
    public void run()
    {
        while(active)
        {
            try
            {
                Thread.sleep(1000);
                if(!active) return;

                counter --;
                tvCount.post(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        tvCount.setText(counter + "");
                    }
                });

                if(counter <= 0)
                {
                    this.setActive(false);
                    if(onCounterFinishedListener != null)
                    {
                        mainHandler.post(new Runnable()
                        {
                            @Override
                            public void run()
                            {
                                onCounterFinishedListener.onCounterFinished();
                            }
                        });
                    }
                }
            }
            catch(InterruptedException e)
            {
                e.printStackTrace();
            }
        }
    }
}
