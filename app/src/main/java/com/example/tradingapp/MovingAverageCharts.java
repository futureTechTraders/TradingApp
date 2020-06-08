package com.example.tradingapp;

import androidx.appcompat.app.AppCompatActivity;

import java.lang.*;
import java.util.ArrayList;

import android.app.DownloadManager;
import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.JsonObject;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.jjoe64.graphview.series.DataPoint;

import org.json.JSONObject;

public class MovingAverageCharts extends AppCompatActivity {

    private final String SMATYPE = "SMA type";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_moving_average_charts);

        //ImageView emaChart = (ImageView) findViewById(R.id.EMA);
        final TextView textView = (TextView) findViewById(R.id.DATA);

        int days = getIntent().getIntExtra("DAYS", 0);

        //Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://192.168.248.106:3000/name?days=20"));
        //startActivity(browserIntent);

        RequestQueue queue = Volley.newRequestQueue(this);
        Log.d(SMATYPE, String.valueOf(days));
        String url = "http://192.168.248.106:3000/name?days=" + String.valueOf(days);
        //String url = "https://developer.android.com/training/volley/simple#java";

        final StringRequest jsonReq = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {

            String data = new String();

            LineGraphSeries<DataPoint> series;
            //GraphView.GraphViewData[] viewData = new GraphView.GraphViewData[59];

            final int SPACES = 14;
            final int NEXTLINE = 26;

            @Override
            public void onResponse(String response) {

                GraphView smaChart = (GraphView) findViewById(R.id.SMA);

                Log.d("Info", response.toString());
                //textView.setText(response.toString().substring(0, 11));
                data = response.toString();

                int startIndex = 6;
                series = new LineGraphSeries<>();

                for(int i = 0; i < 59; i++) {

                    //xVals.add(data.substring(startIndex, startIndex + 10));
                    //yVals.add(Double.parseDouble(data.substring(startIndex + SPACES, startIndex + NEXTLINE)));

                    series.appendData(new DataPoint(i + 1, Double.parseDouble(data.substring(startIndex + SPACES, startIndex + NEXTLINE))), true, 59);
                    //viewData[i] = new GraphView.GraphViewData(i + 1, Double.parseDouble(data.substring(startIndex + SPACES, startIndex + NEXTLINE)));
                    startIndex += NEXTLINE;
                }

                //GraphViewSeries graphSeries = new GraphViewSeries(viewData);

                smaChart.addSeries(series);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                textView.setText(error.toString());
            }
        });

        jsonReq.setShouldCache(false);
        jsonReq.setRetryPolicy(new DefaultRetryPolicy(30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        queue.add(jsonReq);
    }
}
