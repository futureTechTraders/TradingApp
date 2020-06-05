package com.example.tradingapp;

import androidx.appcompat.app.AppCompatActivity;

import java.lang.*;

import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

public class MovingAverageCharts extends AppCompatActivity {

    private final String SMATYPE = "SMA type";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_moving_average_charts);

        ImageView smaChart = (ImageView) findViewById(R.id.SMA);
        ImageView emaChart = (ImageView) findViewById(R.id.EMA);

        int days = getIntent().getIntExtra("DAYS", 0);

        //Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.google.com"));
        //startActivity(browserIntent);
    }
}
