package com.example.tradingapp;

import android.graphics.Color;
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

        final EditText ticker = (EditText) view.findViewById(R.id.ticker);
        final EditText period = (EditText) view.findViewById(R.id.period);
        final EditText interval = (EditText) view.findViewById(R.id.interval);
        final EditText start = (EditText) view.findViewById(R.id.start);
        final EditText end = (EditText) view.findViewById(R.id.end);
        final EditText timeframe = (EditText) view.findViewById(R.id.timeframe);

        Button button = (Button) view.findViewById(R.id.initiateBot);
        final TextView output = (TextView) view.findViewById(R.id.results);

        final GraphView graph = (GraphView) view.findViewById(R.id.graphView);
        graph.getGridLabelRenderer().setGridColor(Color.WHITE);
        graph.getGridLabelRenderer().setHorizontalLabelsColor(Color.WHITE);
        graph.getGridLabelRenderer().setVerticalLabelsColor(Color.WHITE);

        button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                int days = Integer.parseInt(timeframe.getText().toString());

                String url = "https://73d5f2e64836.ngrok.io/name?ticker=" + ticker.getText().toString() + "&period="
                        + period.getText().toString() + "&interval=" + interval.getText().toString() + "&start=" + start.getText().toString() +
                        "&end=" + end.getText().toString() + "&days=" + days + "&indicator=stock_bot";

                RequestQueue queue = Volley.newRequestQueue(getContext());

                final StringRequest jsonReq = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {

                    LineGraphSeries<DataPoint> emaData = new LineGraphSeries<>();
                    LineGraphSeries<DataPoint> smaData = new LineGraphSeries<>();
                    LineGraphSeries<DataPoint> macdData = new LineGraphSeries<>();
                    LineGraphSeries<DataPoint> signalData = new LineGraphSeries<>();

                    @Override
                    public void onResponse(String response) {

                        String[] data = response.split("Name: EMA, dtype: float64");
                        String ema = data[0];

                        data = data[1].split("Name: SMA, dtype: float64");
                        String sma = data[0];

                        String[] data2 = data[1].split("Name: MACD, dtype: float64");
                        String macd = data2[0];
                        String signalWithResult = data2[1];

                        String signal = signalWithResult.substring(0, signalWithResult.indexOf("Name: signal, dtype: float64"));
                        String result = signalWithResult.substring(signalWithResult.indexOf("Name: signal, dtype: float64") + 28);

                        Log.d(TAG, signal);

                        emaData = extractData(ema);
                        smaData = extractData(sma);
                        macdData = extractData(macd);
                        signalData = extractData(signal);

                        emaData.setColor(Color.YELLOW);
                        macdData.setColor(Color.rgb(0, 204, 0));
                        signalData.setColor(Color.rgb(153, 0, 153));

                        graph.addSeries(emaData);
                        graph.addSeries(smaData);
                        graph.addSeries(macdData);
                        graph.addSeries(signalData);

                        Log.d(TAG, result);
                        output.setText(result);

                        graph.getGridLabelRenderer().setVerticalLabelsColor(Color.WHITE);
                        graph.getViewport().setMinY(-20);
                        graph.getViewport().setMaxY(400);
                        graph.getViewport().setXAxisBoundsManual(true);
                        graph.getViewport().setYAxisBoundsManual(true);
                    }

                    public LineGraphSeries<DataPoint> extractData(String response) {

                        LineGraphSeries<DataPoint> data = new LineGraphSeries<>();

                        boolean isNumber = false;
                        int numbers = 0;
                        int dataPoint = 1;
                        int i = 0;
                        int size = 0;

                        while(i < response.length()) {

                            try {

                                Integer.parseInt(Character.toString(response.charAt(i)));

                                if(!isNumber) {

                                    if (numbers % 2 != 0) {

                                        isNumber = true;
                                        i++;
                                        size++;

                                    } else {

                                        dataPoint++;
                                        i += 10;
                                        numbers++;
                                    }
                                }
                                else {

                                    i++;
                                    size++;
                                }

                            } catch(NumberFormatException e) {

                                if(response.charAt(i) == '.') {

                                    i++;
                                    size++;
                                }
                                else if(response.charAt(i) == 'N' && response.charAt(i + 1) == 'a') {

                                    //data.appendData(new DataPoint(dataPoint, 0), true, dataPoint);
                                    i += 3;
                                    numbers++;
                                }
                                else if(response.charAt(i) == '-') {

                                    isNumber = true;
                                    i++;
                                    size++;
                                }
                                else {

                                    if(isNumber) {

                                        double yValue = Double.valueOf(response.substring(i - size, i));
                                        size = 0;
                                        i++;
                                        isNumber = false;
                                        numbers++;

                                        data.appendData(new DataPoint(dataPoint, yValue), true, dataPoint);
                                    }
                                    else {

                                        i++;
                                    }
                                }
                            }
                        }

                        return data;
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
