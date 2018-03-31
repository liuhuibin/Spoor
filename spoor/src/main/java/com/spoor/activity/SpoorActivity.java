package com.spoor.activity;

import android.content.Intent;
import android.net.Uri;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.spoor.LogLevel;
import com.spoor.R;
import com.spoor.Spoor;

import java.io.File;

public class SpoorActivity extends AppCompatActivity {


    private android.support.v7.widget.Toolbar mToolbar;

    private TextView mLogFilePath;
    private Button mShare;
    private Spinner mSpoorLevel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spoor);

        mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mLogFilePath = findViewById(R.id.tv_log_file_path);
        mLogFilePath.setText(Spoor.getInstance().getLogFilePath());

        mShare = findViewById(R.id.btn_share);
        mShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                File logFile = new File(Spoor.getInstance().getLogFilePath());
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                Uri logFileUri = FileProvider.getUriForFile(
                        SpoorActivity.this,
                        "com.spoor.fileprovider",
                        logFile);
                shareIntent.putExtra(Intent.EXTRA_STREAM, logFileUri);
                startActivity(Intent.createChooser(shareIntent, "SEND TO"));
            }
        });

        mSpoorLevel = findViewById(R.id.spinner_spoor_level);
        mSpoorLevel.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        Spoor.getInstance().setMinLevel(LogLevel.INFO);
                        break;
                    case 1:
                        Spoor.getInstance().setMinLevel(LogLevel.DEBUG);
                        break;
                    case 2:
                        Spoor.getInstance().setMinLevel(LogLevel.WARNING);
                        break;
                    case 3:
                        Spoor.getInstance().setMinLevel(LogLevel.ERROR);
                        break;
                    default:
                        Spoor.getInstance().setMinLevel(LogLevel.INFO);
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        int pos = 0;
        switch (Spoor.getInstance().getMinLevel()) {
            case INFO:
                pos = 0;
                break;
            case DEBUG:
                pos = 1;
                break;
            case WARNING:
                pos = 2;
                break;
            case ERROR:
                pos = 3;
                break;
        }
        mSpoorLevel.setSelection(pos);

    }

}
