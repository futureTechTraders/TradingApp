package com.example.tradingapp;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.Date;

import javax.xml.transform.ErrorListener;

public class WatchlistAdapter extends RecyclerView.Adapter<WatchlistAdapter.ViewHolder> {

    final String TAG = "Adapter";

    List<String> ticker;
    StockClosingPrice prevClosingPrice;
    Context context;

    public WatchlistAdapter(List<String> ticker, StockClosingPrice prevClosingPrice, Context context) {

        this.ticker = ticker;
        this.prevClosingPrice = prevClosingPrice;
        this.context = context;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView ticker;
        public TextView closePrice;
        public TextView percentChange;

        public ViewHolder(@NonNull View itemView) {

            super(itemView);

            ticker = (TextView) itemView.findViewById(R.id.ticker);
            closePrice = (TextView) itemView.findViewById(R.id.closePrice);
            percentChange = (TextView) itemView.findViewById(R.id.percentChange);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        Context parentContext = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(parentContext);

        View view = inflater.inflate(R.layout.stock_watchlist_item, parent, false);

        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        TextView holderTicker = (TextView) holder.ticker;
        final TextView holderPercentChange = (TextView) holder.percentChange;
        final TextView holderClosePrice = (TextView) holder.closePrice;

        String url = "https://9dad08d01cef.ngrok.io/name?ticker=" + ticker.get(position) + "&indicator=watchlist";

        RequestQueue queue = Volley.newRequestQueue(context);

        final StringRequest jsonReq = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                //Log.d(TAG, response);

                String[] data = response.split("@", 6);
                ArrayList<Double> stockInformation = extractData(response);

                if(stockInformation.size() == 10) {

                    holderClosePrice.setText(String.valueOf(stockInformation.get(1)));
                    double percentChange = (stockInformation.get(1) - stockInformation.get(0)) / stockInformation.get((0));

                    if(percentChange > 0) {

                        holderPercentChange.setText(String.valueOf(percentChange));
                        holderPercentChange.setTextColor(Color.GREEN);
                    } else {

                        holderPercentChange.setText(String.valueOf(percentChange));
                        holderPercentChange.setTextColor(Color.RED);
                    }
                } else {

                    holderClosePrice.setText(String.valueOf(stockInformation.get(0)));

                    if(prevClosingPrice.getPrice() == 0) {

                        holderPercentChange.setText("N/A");

                    } else {

                        double percentChange = (stockInformation.get(0) - prevClosingPrice.getPrice()) / prevClosingPrice.getPrice();

                        if(percentChange > 0) {

                            holderPercentChange.setText(String.valueOf(percentChange));
                            holderPercentChange.setTextColor(Color.GREEN);
                        } else {

                            holderPercentChange.setText(String.valueOf(percentChange));
                            holderPercentChange.setTextColor(Color.RED);
                        }

                        prevClosingPrice.setPrice(stockInformation.get(0));
                    }
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

                Log.d(TAG, error.toString());
            }
        });

        jsonReq.setShouldCache(false);
        jsonReq.setRetryPolicy(new DefaultRetryPolicy(30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        queue.add(jsonReq);

        holderTicker.setText(ticker.get(position));
        //holderPercentChange.setText(percentChange.get(position).toString());
        //holder.closePrice.setText(closePrices.get(position).toString());
    }

    @Override
    public int getItemCount() {

        return ticker.size();
    }

    private ArrayList<Double> extractData(String response) {

        String[] data = response.split("@", 6);
        ArrayList<Double> stockInformation = new ArrayList<>();

        for(int i = 0; i < data.length; i++) {

            int traverse = 0;
            boolean isNumber = false;
            int dataPoint = 0;
            int size = 0;
            int num = 0;

            while(traverse < data[i].length()) {

                try {

                    int num2 = Integer.parseInt(Character.toString(data[i].charAt(traverse)));

                    if(!isNumber) {

                        if(num % 2 == 0) {

                            traverse += 10;
                            dataPoint++;
                            num++;

                        } else {

                            Log.d(TAG, "Info Extracted");
                            isNumber = true;
                            size++;
                            traverse++;
                        }

                    } else {

                        size++;
                        traverse++;
                    }
                } catch(NumberFormatException e) {

                    if(data[i].charAt(traverse) == '.') {

                        //Log.d(TAG, "PERIOD");
                        size++;
                        traverse++;

                    } else if (isNumber){

                        //Log.d(TAG, data[i].substring(traverse - size, traverse));
                        stockInformation.add(Double.parseDouble(data[i].substring(traverse - size, traverse)));
                        traverse++;
                        num++;
                        size = 0;
                        isNumber = false;

                    } else {

                        traverse++;
                    }
                }
            }
        }

        return stockInformation;
    }
}
