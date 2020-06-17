package com.example.tradingapp;

import android.os.Bundle;
import android.os.TestLooperManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
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
import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;

import org.w3c.dom.Text;

public class MovingAveragesFragment extends Fragment {

    String[] LIST = {"SMA", "EMA"};
    private EditText editText;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        final View fragmentView = inflater.inflate(R.layout.fragment_sma_ema, container, false);

        Button button = (Button) fragmentView.findViewById(R.id.button);
        editText = (EditText) fragmentView.findViewById(R.id.editText);
        final TextView textView = (TextView) fragmentView.findViewById(R.id.textView);
        final GraphView smaChart = (GraphView) fragmentView.findViewById(R.id.SMA);

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_dropdown_item, LIST);

        final MaterialBetterSpinner betterSpinner = (MaterialBetterSpinner) fragmentView.findViewById(R.id.ema_sma_drop_down);
        betterSpinner.setAdapter(arrayAdapter);

        button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(final View v) {

                int days = Integer.parseInt(editText.getText().toString());

                RequestQueue queue = Volley.newRequestQueue(getContext());
                Log.d("DAYS", String.valueOf(days));
                String url = "https://cb3c50999667.ngrok.io/name?days=" + String.valueOf(days) + "&indicator=sma_ema";

                final StringRequest jsonReq = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {

                    String data = new String();

                    LineGraphSeries<DataPoint> series;
                    //GraphView.GraphViewData[] viewData = new GraphView.GraphViewData[59];

                    final int SPACES = 14;
                    final int NEXTLINE = 26;

                    @Override
                    public void onResponse(String response) {

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

                        Log.e("ERROR", error.toString());
                    }
                });

                jsonReq.setShouldCache(false);
                jsonReq.setRetryPolicy(new DefaultRetryPolicy(30000,
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

                queue.add(jsonReq);
            }
        });

        return fragmentView;
    }
}
