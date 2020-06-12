package com.example.tradingapp;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class HomeFragment extends Fragment {

    ArrayList<String> links = new ArrayList<>();
    ArrayList<String> description = new ArrayList<>();
    ArrayList<Bitmap> thumbnails = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home, container, false);
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);

        links.add("https://www.youtube.com/watch?v=VKsiM2SrH0E&t=2s");
        links.add("https://www.youtube.com/watch?v=lwvylfBPLFg&t=44s");
        links.add("https://www.youtube.com/watch?v=lT8UNWmVLHM");

        description.add("In this video, you will be investigating the basics of supply and demand in economics.");
        description.add("This video is an extension to the previous supply and demand video, where we start to analyze markets based off of supply and demand.");
        description.add("This video features direct explanation of python code that works with WebScraping to create a Stock Bot");

        Bitmap supply_demand = BitmapFactory.decodeResource(getContext().getResources(), R.drawable.supple_demand);
        Bitmap analyze = BitmapFactory.decodeResource(getContext().getResources(), R.drawable.analyze);
        Bitmap cs_module_1 = BitmapFactory.decodeResource(getContext().getResources(), R.drawable.cs_module_1);

        thumbnails.add(supply_demand);
        thumbnails.add(analyze);
        thumbnails.add(cs_module_1);

        RecyclerAdapter recyclerAdapter = new RecyclerAdapter(description, links, thumbnails);

        recyclerView.setAdapter(recyclerAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        return view;
    }
}
