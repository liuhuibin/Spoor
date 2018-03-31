package com.spoor.example.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;

import com.spoor.LogLevel;
import com.spoor.Spoor;
import com.spoor.activity.SpoorActivity;
import com.spoor.example.R;

public class MainActivity extends AppCompatActivity {

    Button mLog1;
    Button mLog2;
    Button mLog3;
    Button mSpoorManagerBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mLog1 = findViewById(R.id.btn_log1);
        mLog2 = findViewById(R.id.btn_log2);
        mLog3 = findViewById(R.id.btn_log3);
        mSpoorManagerBtn = findViewById(R.id.btn_spoor);

        mLog1.setOnClickListener(v -> saveSomething());
        mLog2.setOnClickListener(v -> Spoor.getInstance().e("This is a too loooooooooooooooooooooooooooooooooooooooooooooong log"));
        mLog3.setOnClickListener(v-> saveSomething2());

        mSpoorManagerBtn.setOnClickListener(v -> startActivity(new Intent(this, SpoorActivity.class)));

        Spoor.getInstance().setMinLevel(LogLevel.WARNING);

    }

    private void  saveSomething() {
        Spoor.getInstance().i("Info log!");
        Spoor.getInstance().log(LogLevel.INFO, "Info log!->by log");

        Spoor.getInstance().d("Debug log!");
        Spoor.getInstance().log(LogLevel.DEBUG, "Debug log!->by log");

        Spoor.getInstance().w("Warning log!");
        Spoor.getInstance().log(LogLevel.WARNING, "Warning log!->by log");

        Spoor.getInstance().e("Error log!");
        Spoor.getInstance().log(LogLevel.ERROR, "Error log!->by log");
    }

    private void  saveSomething2() {

        new Thread(() -> {
            for (int i =0 ; i<100; i++) {
                int finalI = i;
                new Thread(() -> {

                    switch (finalI%4) {
                        case 0:
                            Spoor.getInstance().i("name=" + Thread.currentThread().getName() + "log 0");
                            break;
                        case 1:
                            Spoor.getInstance().i("name=" + Thread.currentThread().getName() + "log 1");
                            break;
                        case 2:
                            Spoor.getInstance().i("name=" + Thread.currentThread().getName() + "log 2");
                            break;
                        case 3:
                            Spoor.getInstance().i("name=" + Thread.currentThread().getName() + "log 3");
                            break;
                    }

                }).start();
            }
        }).start();

    }
}
