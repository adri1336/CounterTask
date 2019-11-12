package com.example.countertask;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.example.countertask.model.AsyncTaskCounter;
import com.example.countertask.model.OnCounterFinishedListener;
import com.example.countertask.model.ThreadedCounter;
import com.example.countertask.view.MainActivityModel;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class MainActivity extends AppCompatActivity
{
    private MainActivityModel model;
    private TextView tvCount;
    private TextInputLayout tilCountStartValue;
    private TextInputEditText tietCountStartValue;
    private RadioGroup radioGroup;
    private Button btToggleTask;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        model = ViewModelProviders.of(this).get(MainActivityModel.class);

        tvCount = findViewById(R.id.tvCount);
        tilCountStartValue = findViewById(R.id.tilCountStartValue);
        tietCountStartValue = findViewById(R.id.tietCountStartValue);
        radioGroup = findViewById(R.id.radioGroup);
        btToggleTask = findViewById(R.id.btToggleTask);

        tietCountStartValue.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after)
            {
                model.setError(null);
                tilCountStartValue.setError(model.getError());
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {

            }

            @Override
            public void afterTextChanged(Editable s)
            {

            }
        });

        if(model.getRadioButtonId() == 0)
            model.setRadioButtonId(radioGroup.getCheckedRadioButtonId());

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId)
            {
                if(!model.isToggle())
                    model.setRadioButtonId(checkedId);
            }
        });

        btToggleTask.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Editable editable = tietCountStartValue.getText();
                if(editable == null || editable.length() <= 0) model.setStartCount(0);
                else model.setStartCount(Integer.parseInt(editable.toString()));

                if(model.getStartCount() <= 0)
                {
                    model.setError("Valor inicial no válido");
                    tilCountStartValue.setError(model.getError());
                }
                else
                {
                    model.setToggle(!model.isToggle());
                    if(model.isToggle())
                    {
                        tvCount.setText(model.getStartCount() + "");
                        if(model.getRadioButtonId() == R.id.rbThread)
                        {
                            model.setThreadedCounter(new ThreadedCounter(model.getStartCount(), tvCount, MainActivity.this));
                            model.getThreadedCounter().start();
                            model.getThreadedCounter().setOnCounterFinishedListener(new OnCounterFinishedListener()
                            {
                                @Override
                                public void onCounterFinished()
                                {
                                    model.setThreadedCounter(null);
                                    model.setStartCount(0);
                                    tvCount.setText(model.getStartCount() + "");
                                    tietCountStartValue.setText(model.getStartCount() + "");
                                    model.setToggle(false);

                                    Intent intent = new Intent(MainActivity.this, EndActivity.class);
                                    startActivity(intent);
                                }
                            });
                            setRadioButtonsEnabled(false);
                        }
                        else if(model.getRadioButtonId() == R.id.rbAsyncTask)
                        {
                            model.setAsyncTaskCounter(new AsyncTaskCounter(model.getStartCount(), tvCount, MainActivity.this));
                            model.getAsyncTaskCounter().execute();
                            model.getAsyncTaskCounter().setOnCounterFinishedListener(new OnCounterFinishedListener()
                            {
                                @Override
                                public void onCounterFinished()
                                {
                                    model.setAsyncTaskCounter(null);
                                    model.setStartCount(0);
                                    tvCount.setText(model.getStartCount() + "");
                                    tietCountStartValue.setText(model.getStartCount() + "");
                                    model.setToggle(false);

                                    Intent intent = new Intent(MainActivity.this, EndActivity.class);
                                    startActivity(intent);
                                }
                            });
                            setRadioButtonsEnabled(false);
                        }
                    }
                    else
                    {
                        if(model.getThreadedCounter() != null)
                        {
                            model.getThreadedCounter().setActive(false);
                            model.setThreadedCounter(null);
                        }
                        else if(model.getAsyncTaskCounter() != null)
                        {
                            model.getAsyncTaskCounter().setActive(false);
                            model.setAsyncTaskCounter(null);
                        }

                        //desactivado
                        model.setStartCount(0);
                        tvCount.setText(model.getStartCount() + "");
                        tietCountStartValue.setText(model.getStartCount() + "");
                        setRadioButtonsEnabled(true);
                    }
                    btToggleTask.setText(model.isToggle() ? "Stop" : "Start");
                }
            }
        });

        ThreadedCounter threadedCounter = model.getThreadedCounter();
        AsyncTaskCounter asyncTaskCounter = model.getAsyncTaskCounter();
        if(threadedCounter != null)
        {
            threadedCounter.setTvCount(tvCount);
            threadedCounter.setContext(this);
        }
        else if(asyncTaskCounter != null)
        {
            asyncTaskCounter.setTvCount(tvCount);
            asyncTaskCounter.setContext(this);
        }
    }

    @Override
    protected void onResume()
    {
        super.onResume();

        tilCountStartValue.setError(model.getError());
        tietCountStartValue.setText(model.getStartCount() + "");
        radioGroup.check(model.getRadioButtonId());
        btToggleTask.setText(model.isToggle() ? "Stop" : "Start");

        if(model.isToggle()) setRadioButtonsEnabled(false);
        else setRadioButtonsEnabled(true);

        if(model.getThreadedCounter() != null) tvCount.setText(model.getThreadedCounter().getCounter() + "");
        else if(model.getAsyncTaskCounter() != null) tvCount.setText(model.getAsyncTaskCounter().getCounter() + "");
        else tvCount.setText(model.getStartCount() + "");
    }

    private void setRadioButtonsEnabled(boolean toggle)
    {
        for(int i = 0; i < radioGroup.getChildCount(); i++)
        {
            radioGroup.getChildAt(i).setEnabled(toggle);
        }
    }
}
