package com.example.countertask.model;

import android.widget.TextView;

public class ThreadedCounter extends Thread
{
    private int counter;
    private TextView tvCount;
    private boolean active;
    private OnCounterFinishedListener onCounterFinishedListener;

    public ThreadedCounter(int counter, TextView tvCount)
    {
        this.counter = counter;
        this.tvCount = tvCount;
        this.active = true;
    }

    public void setActive(boolean active)
    {
        this.active = active;
    }

    public void setOnCounterFinishedListener(OnCounterFinishedListener onCounterFinishedListener)
    {
        this.onCounterFinishedListener = onCounterFinishedListener;
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
                        onCounterFinishedListener.onCounterFinished();
                }
            }
            catch(InterruptedException e)
            {
                e.printStackTrace();
            }
        }
    }

    public interface OnCounterFinishedListener
    {
        void onCounterFinished();
    }
}
