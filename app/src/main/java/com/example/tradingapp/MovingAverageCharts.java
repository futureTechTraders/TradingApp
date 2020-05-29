package com.example.tradingapp;

import androidx.appcompat.app.AppCompatActivity;

import android.media.Image;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

public class MovingAverageCharts extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_moving_average_charts);

        ImageView smaChart = (ImageView) findViewById(R.id.SMA);
        ImageView emaChart = (ImageView) findViewById(R.id.EMA);

        int days = getIntent().getIntExtra("DAYS", 0);
    }
}
