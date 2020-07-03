package com.example.tradingapp;

import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.github.mikephil.charting.charts.CandleStickChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.CandleData;
import com.github.mikephil.charting.data.CandleDataSet;
import com.github.mikephil.charting.data.CandleEntry;
import com.google.android.material.textfield.TextInputEditText;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class StockBotFragment extends Fragment {

    final String TAG = "StockBotFragment";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_stock_bot, container, false);

        final TextInputEditText ticker = (TextInputEditText) view.findViewById(R.id.ticker);
        final TextInputEditText period = (TextInputEditText) view.findViewById(R.id.period);
        final TextInputEditText interval = (TextInputEditText) view.findViewById(R.id.interval);
        final TextInputEditText start = (TextInputEditText) view.findViewById(R.id.start);
        final TextInputEditText end = (TextInputEditText) view.findViewById(R.id.end);
        final TextInputEditText timeframe = (TextInputEditText) view.findViewById(R.id.timeframe);

        Button button = (Button) view.findViewById(R.id.initiateBot);
        //final TextView output = (TextView) view.findViewById(R.id.results);

        final CandleStickChart candleStickChart = (CandleStickChart) view.findViewById(R.id.candle_stick_chart);
        candleStickChart.setHighlightPerDragEnabled(true);
        candleStickChart.setNoDataText("");
        candleStickChart.getDescription().setEnabled(false);

        YAxis yAxis = candleStickChart.getAxisLeft();
        YAxis rightAxis = candleStickChart.getAxisRight();
        yAxis.setDrawGridLines(false);
        rightAxis.setDrawGridLines(false);
        candleStickChart.requestDisallowInterceptTouchEvent(false);

        XAxis xAxis = candleStickChart.getXAxis();

        xAxis.setDrawGridLines(false);// disable x axis grid lines
        xAxis.setDrawLabels(false);
        rightAxis.setTextColor(Color.WHITE);
        yAxis.setDrawLabels(false);
        xAxis.setGranularity(1f);
        xAxis.setGranularityEnabled(true);
        xAxis.setAvoidFirstLastClipping(true);

        Legend l = candleStickChart.getLegend();
        l.setEnabled(true);

        /*
        final GraphView graph = (GraphView) view.findViewById(R.id.graphView);
        graph.getGridLabelRenderer().setGridColor(Color.WHITE);
        graph.getGridLabelRenderer().setHorizontalLabelsColor(Color.WHITE);
        graph.getGridLabelRenderer().setVerticalLabelsColor(Color.WHITE);
         */

        button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                int days = Integer.parseInt(timeframe.getText().toString());

                String url = "https://0586c288ea0e.ngrok.io/name?ticker=" + ticker.getText().toString() + "&period="
                        + period.getText().toString() + "&interval=" + interval.getText().toString() + "&start=" + start.getText().toString() +
                        "&end=" + end.getText().toString() + "&days=" + days + "&indicator=stock_bot";

                RequestQueue queue = Volley.newRequestQueue(getContext());

                final StringRequest jsonReq = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {

                    /*
                    LineGraphSeries<DataPoint> emaData = new LineGraphSeries<>();
                    LineGraphSeries<DataPoint> smaData = new LineGraphSeries<>();
                    LineGraphSeries<DataPoint> macdData = new LineGraphSeries<>();
                    LineGraphSeries<DataPoint> signalData = new LineGraphSeries<>();
                     */

                    ArrayList<Double> close = new ArrayList<>();
                    ArrayList<Double> open = new ArrayList<>();
                    ArrayList<Double> high = new ArrayList<>();
                    ArrayList<Double> low = new ArrayList<>();

                    ArrayList<CandleEntry> candleEntries = new ArrayList<>();

                    @Override
                    public void onResponse(String response) {

                        Log.d(TAG, response);

                        String data[] = response.split("@", 6);

                        close = extractData(data[0]);
                        open = extractData(data[1]);
                        high = extractData(data[2]);
                        low = extractData(data[3]);

                        for(int i = 0; i < close.size(); i++) {

                            candleEntries.add(new CandleEntry(i, high.get(i).floatValue(), low.get(i).floatValue(), open .get(i).floatValue(), close.get(i).floatValue()));
                        }

                        CandleDataSet set1 = new CandleDataSet(candleEntries, "DataSet 1");
                        set1.setColor(Color.rgb(80, 80, 80));
                        set1.setShadowWidth(0.8f);
                        set1.setDecreasingColor(getResources().getColor(R.color.red));
                        set1.setDecreasingPaintStyle(Paint.Style.FILL);
                        set1.setIncreasingColor(getResources().getColor(R.color.colorAccent));
                        set1.setIncreasingPaintStyle(Paint.Style.FILL);
                        set1.setNeutralColor(Color.LTGRAY);
                        set1.setDrawValues(false);


                        // create a data object with the datasets
                        CandleData candleData = new CandleData(set1);
                        // set data
                        candleStickChart.setData(candleData);
                        candleStickChart.invalidate();

                        //output.setText(data[4]);
                    }

                    public ArrayList<Double> extractData(String data) {

                        ArrayList<Double> stockInformation = new ArrayList<>();

                        int traverse = 0;
                        boolean isNumber = false;
                        int dataPoint = 0;
                        int size = 0;
                        int num = 0;

                        while(traverse < data.length()) {

                            try {

                                int num2 = Integer.parseInt(Character.toString(data.charAt(traverse)));

                                if(!isNumber) {

                                    if(num % 2 == 0) {

                                        traverse += 10;
                                        dataPoint++;
                                        num++;

                                    } else {

                                        //Log.d(TAG, "Info Extracted");
                                        isNumber = true;
                                        size++;
                                        traverse++;
                                    }

                                } else {

                                    size++;
                                    traverse++;
                                }
                            } catch(NumberFormatException e) {

                                if(data.charAt(traverse) == '.') {

                                    //Log.d(TAG, "PERIOD");
                                    size++;
                                    traverse++;

                                } else if (isNumber){

                                    //Log.d(TAG, data[i].substring(traverse - size, traverse));
                                    stockInformation.add(Double.parseDouble(data.substring(traverse - size, traverse)));
                                    traverse++;
                                    num++;
                                    size = 0;
                                    isNumber = false;

                                } else {

                                    traverse++;
                                }
                            }
                        }

                        return stockInformation;
                    }

                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        Log.e(TAG, error.toString());
                    }
                });

                jsonReq.setShouldCache(false);
                jsonReq.setRetryPolicy(new DefaultRetryPolicy(30000,
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

                queue.add(jsonReq);
            }
        });

        return view;
    }
}
