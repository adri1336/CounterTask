package com.example.countertask;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.countertask.model.ThreadedCounter;
import com.example.countertask.view.MainActivityModel;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class MainActivity extends AppCompatActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final MainActivityModel model = ViewModelProviders.of(this).get(MainActivityModel.class);

        final TextView tvCount = findViewById(R.id.tvCount);
        final TextInputLayout tilCountStartValue = findViewById(R.id.tilCountStartValue);
        final TextInputEditText tietCountStartValue = findViewById(R.id.tietCountStartValue);
        final Button btToggleTask = findViewById(R.id.btToggleTask);

        tvCount.setText(model.getStartCount() + "");
        tilCountStartValue.setError(model.getError());
        tietCountStartValue.setText(model.getStartCount() + "");
        btToggleTask.setText(model.isToggle() ? "Stop" : "Start");

        tietCountStartValue.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after)
            {
                model.setError(null);
                tilCountStartValue.setError(null);
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
                        //activado
                        tvCount.setText(model.getStartCount() + "");
                        model.setThreadedCounter(new ThreadedCounter(model.getStartCount(), tvCount));
                        model.getThreadedCounter().start();
                        model.getThreadedCounter().setOnCounterFinishedListener(new ThreadedCounter.OnCounterFinishedListener()
                        {
                            @Override
                            public void onCounterFinished()
                            {
                                model.setStartCount(0);
                                tvCount.setText(model.getStartCount() + "");
                                tietCountStartValue.setText(model.getStartCount() + "");
                                model.setToggle(false);

                                Intent intent = new Intent(MainActivity.this, EndActivity.class);
                                startActivity(intent);
                            }
                        });
                    }
                    else
                    {
                        if(model.getThreadedCounter() != null)
                            model.getThreadedCounter().setActive(false);

                        //desactivado
                        model.setStartCount(0);
                        tvCount.setText(model.getStartCount() + "");
                        tietCountStartValue.setText(model.getStartCount() + "");
                    }
                    btToggleTask.setText(model.isToggle() ? "Stop" : "Start");
                }
            }
        });
    }
}
