package com.example.tradingapp;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Outline;
import android.media.Image;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewOutlineProvider;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.poliveira.parallaxrecyclerview.ParallaxRecyclerAdapter;

import java.util.ArrayList;

public class HomeFragment extends Fragment {

    ArrayList<String> links = new ArrayList<>();
    ArrayList<String> description = new ArrayList<>();
    ArrayList<Bitmap> thumbnails = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.fragment_home, container, false);
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(manager);

        DividerItemDecoration itemDecor = new DividerItemDecoration(getContext(), DividerItemDecoration.HORIZONTAL);
        recyclerView.addItemDecoration(itemDecor);

        links.add("https://www.youtube.com/watch?v=VKsiM2SrH0E&t=2s");
        links.add("https://www.youtube.com/watch?v=lwvylfBPLFg&t=44s");
        links.add("https://www.youtube.com/watch?v=lT8UNWmVLHM");
        links.add("https://www.youtube.com/watch?v=H4EOXhn5wxQ");

        description.add("In this video, you will be investigating the basics of supply and demand in economics.");
        description.add("This video is an extension to the previous supply and demand video, where we start to analyze markets based off of supply and demand.");
        description.add("This video features direct explanation of python code that works with WebScraping to create a Stock Bot");
        description.add("In this video, you will be exploring the intuitions behind how stocks and the stock market works");

        Bitmap supply_demand = BitmapFactory.decodeResource(getContext().getResources(), R.drawable.supple_demand);
        Bitmap analyze = BitmapFactory.decodeResource(getContext().getResources(), R.drawable.analyze);
        Bitmap cs_module_1 = BitmapFactory.decodeResource(getContext().getResources(), R.drawable.cs_module_1);
        Bitmap stock_market = BitmapFactory.decodeResource(getContext().getResources(), R.drawable.stock_market);

        thumbnails.add(supply_demand);
        thumbnails.add(analyze);
        thumbnails.add(cs_module_1);
        thumbnails.add(stock_market);

        ParallaxRecyclerAdapter<String> stringAdapter = new ParallaxRecyclerAdapter<String>(links) {

            @Override
            public void onBindViewHolderImpl(RecyclerView.ViewHolder viewHolder, ParallaxRecyclerAdapter<String> parallaxRecyclerAdapter, int i) {

                ((ViewHolder) viewHolder).linkText.setText(links.get(i));
                ((ViewHolder) viewHolder).descriptionText.setText(description.get(i));

                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

                    ViewOutlineProvider provider = new ViewOutlineProvider() {

                        @Override
                        public void getOutline(View view, Outline outline) {

                            int curveRadius = 24;
                            outline.setRoundRect(0, 0, view.getWidth(), (view.getHeight()), curveRadius);
                        }
                    };

                    ((ViewHolder) viewHolder).thumbnail.setOutlineProvider(provider);
                }
                ((ViewHolder) viewHolder).thumbnail.setImageBitmap(thumbnails.get(i));
            }

            @Override
            public RecyclerView.ViewHolder onCreateViewHolderImpl(ViewGroup viewGroup, ParallaxRecyclerAdapter<String> parallaxRecyclerAdapter, int i) {

                return new ViewHolder(getLayoutInflater().inflate(R.layout.video_article_view, viewGroup, false));
            }

            @Override
            public int getItemCountImpl(ParallaxRecyclerAdapter<String> parallaxRecyclerAdapter) {

                return links.size();
            }
        };

        //RecyclerAdapter recyclerAdapter = new RecyclerAdapter(description, links, thumbnails);
        stringAdapter.setParallaxHeader(getLayoutInflater().inflate(R.layout.my_header, recyclerView, false), recyclerView);

        recyclerView.setAdapter(stringAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        return view;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView linkText;
        public TextView descriptionText;
        public ImageView thumbnail;

        public ViewHolder(View itemView) {

            super(itemView);

            linkText = (TextView) itemView.findViewById(R.id.linkText);
            descriptionText = (TextView) itemView.findViewById(R.id.descriptionText);
            thumbnail = (ImageView) itemView.findViewById(R.id.thumbnail);
        }
    }
}
