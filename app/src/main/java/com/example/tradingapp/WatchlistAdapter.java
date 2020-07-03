package com.example.tradingapp;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.w3c.dom.Text;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Date;
import java.util.ListIterator;

import javax.xml.transform.ErrorListener;

public class WatchlistAdapter extends RecyclerView.Adapter<WatchlistAdapter.ViewHolder> {

    final String TAG = "Adapter";

    ArrayList<String> ticker;
    List<StockClosingPrice> prevClosingPrices;
    Context context;

    ArrayList<Double> dataPoints = new ArrayList<>();

    public WatchlistAdapter(ArrayList<String> ticker, List<StockClosingPrice> prevClosingPrices, Context context) {

        this.ticker = ticker;
        this.prevClosingPrices = prevClosingPrices;
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

        final int index = position;

        for(int i = 0; i < ticker.size(); i++) {

            Log.d(TAG, ticker.get(i));
        }

        TextView holderTicker = (TextView) holder.ticker;
        final TextView holderPercentChange = (TextView) holder.percentChange;
        final TextView holderClosePrice = (TextView) holder.closePrice;

        //Log.d(TAG, ticker.get(position));

        String url = "https://0586c288ea0e.ngrok.io/name?ticker=" + ticker.get(position) + "&indicator=watchlist";

        RequestQueue queue = Volley.newRequestQueue(context);

        final StringRequest jsonReq = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.d(TAG, response);

                String[] data = response.split("@", 6);
                ArrayList<Double> stockInformation = extractData(response);

                if(data[0].indexOf("Data doesn't exist") > 0) {

                    if(prevClosingPrices.get(index).getPrice() == 0) {

                        holderPercentChange.setText("N/A");
                        holderPercentChange.setTextColor(Color.WHITE);
                        holderClosePrice.setText("N/A");
                    }
                    else {

                        holderPercentChange.setText("N/A");
                        holderClosePrice.setText(String.valueOf(prevClosingPrices.get(index).getPrice()));
                    }

                    Toast toast = Toast.makeText(context, "Data unavailble at this specific time", Toast.LENGTH_SHORT);
                    toast.show();
                }
                else if(stockInformation.size() == 10) {

                    if(!(dataPoints.indexOf(stockInformation.get(1)) >= 0)) {

                        dataPoints.add(stockInformation.get(1));
                    }

                    holderClosePrice.setText(String.valueOf(stockInformation.get(1)));
                    double percentChange = (stockInformation.get(1) - stockInformation.get(0)) / stockInformation.get((0));
                    DecimalFormat df = new DecimalFormat("###.###");

                    if(percentChange > 0) {

                        holderPercentChange.setText(String.valueOf(df.format(percentChange)) + "%");
                        holderPercentChange.setTextColor(Color.GREEN);
                    } else {

                        holderPercentChange.setText(String.valueOf(df.format(percentChange)) + "%");
                        holderPercentChange.setTextColor(Color.RED);
                    }
                }
                else {

                    if(!(dataPoints.indexOf(stockInformation.get(1)) >= 0)) {

                        dataPoints.add(stockInformation.get(1));
                    }

                    holderClosePrice.setText(String.valueOf(stockInformation.get(0)));

                    if(prevClosingPrices.get(index).getPrice() == 0) {

                        holderPercentChange.setText("N/A");

                    } else {

                        double percentChange = ((stockInformation.get(0) - prevClosingPrices.get(index).getPrice()) / prevClosingPrices.get(index).getPrice()) * 100;
                        DecimalFormat df = new DecimalFormat("###.###");

                        if(percentChange > 0) {

                            holderPercentChange.setText(String.valueOf(df.format(percentChange)) + "%");
                            holderPercentChange.setTextColor(Color.GREEN);
                        } else {

                            holderPercentChange.setText(String.valueOf(df.format(percentChange)) + "%");
                            holderPercentChange.setTextColor(Color.RED);
                        }
                    }

                    prevClosingPrices.get(index).setPrice(stockInformation.get(0));
                    Log.d(TAG, ticker.get(index) + String.valueOf(prevClosingPrices.get(index).getPrice()));
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

        Log.d(TAG, "Item Count" + String.valueOf(ticker.size()));
        return ticker.size();
    }

    private ArrayList<Double> extractData(String response) {

        String[] data = response.split("@", 6);
        ArrayList<Double> stockInformation = new ArrayList<>();

        for(int i = 1; i < data.length; i++) {

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

    public void addItem(String ticker, StockClosingPrice closingPrice) {

        this.ticker.add(ticker);
        prevClosingPrices.add(closingPrice);
    }

    public ArrayList<Double> getDataPoints() {

        return dataPoints;
    }

    public ArrayList<String> getTicker() {

        return ticker;
    }
}
