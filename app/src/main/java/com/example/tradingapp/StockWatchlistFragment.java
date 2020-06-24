package com.example.tradingapp;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Layout;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class StockWatchlistFragment extends Fragment {

    final String TAG = "Watchlist";

    ArrayList<String> ticker = new ArrayList<>();
    ArrayList<StockClosingPrice> prevClosingPrices = new ArrayList<>();

    String tickerSymbol;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.fragment_stock_watchlist, container, false);
        final RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.watchListRecycler);
        final WatchlistAdapter recyclerAdapter = new WatchlistAdapter(ticker, prevClosingPrices, getActivity());
        //recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(recyclerAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getActivity()));

        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.addButton);
        fab.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Add Ticker");

                LayoutInflater layoutInflater = getLayoutInflater();
                View dialogView = layoutInflater.inflate(R.layout.popup_window, null);

                builder.setView(dialogView);

                //LinearLayout layout = new LinearLayout(getContext());
                //layout.setOrientation(LinearLayout.VERTICAL);
                //final EditText editText = new EditText(getContext());
                //editText.setHint("Add Ticker Symbol");
                final EditText editText = (EditText) dialogView.findViewById(R.id.tickerEnter);

                builder.setPositiveButton("Add", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        tickerSymbol = editText.getText().toString();
                        StockClosingPrice prevPrice = new StockClosingPrice();

                        recyclerAdapter.addItem(tickerSymbol, prevPrice);
                        recyclerAdapter.notifyDataSetChanged();
                    }
                });

                final AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

        return view;
    }
}
