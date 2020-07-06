package com.example.tradingapp;

import android.os.Bundle;
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

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.w3c.dom.Text;

public class RiskCalculator extends Fragment {

    final String TAG = "RiskCalculator";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_risk_calculator, container, false);

        final EditText accountSize = (EditText) view.findViewById(R.id.accountSize);
        final EditText accountRisk = (EditText) view.findViewById(R.id.accountRisk);
        final EditText targetPrice = (EditText) view.findViewById(R.id.targetPrice);
        final EditText entryPrice = (EditText) view.findViewById(R.id.entryPrice);
        final EditText stopLoss = (EditText) view.findViewById(R.id.stopLoss);

        final TextView output = (TextView) view.findViewById(R.id.risk_calculated);

        Button button = (Button) view.findViewById(R.id.calculate_risk);

        button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                String url = "https://0586c288ea0e.ngrok.io/name?indicator=risk_calculator&accountSize=" + accountSize.getText() + "&accountRisk="
                        + accountRisk.getText() + "&targetPrice=" + targetPrice.getText() + "&entryPrice" + entryPrice.getText() + "&stopLoss=" + stopLoss.getText();

                RequestQueue requestQueue = Volley.newRequestQueue(getContext());

                final StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {

                        output.setText(response);
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {

                        Log.d(TAG, error.toString());
                        output.setText(error.toString());
                    }
                });//
            }
        });

        return view;
    }
}
