package com.example.tradingapp;

import android.content.Context;
import android.graphics.Bitmap;
import android.media.Image;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Text;

import java.util.List;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {

    private List<String> mDescription;
    private List<String> mLink;
    private List<Bitmap> mThumbnails;

    final String TAG = "RecyclerAdapter";

    public RecyclerAdapter(List<String> description, List<String> link, List<Bitmap> thumbnails) {

        mDescription = description;
        mLink = link;
        mThumbnails = thumbnails;

        Log.d(TAG, mLink.get(0));
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView descriptionView;
        public TextView textView;
        public ImageView imageView;


        public ViewHolder(@NonNull View itemView) {

            super(itemView);

            descriptionView = (TextView) itemView.findViewById(R.id.descriptionText);
            textView = (TextView) itemView.findViewById(R.id.linkText);
            imageView = (ImageView) itemView.findViewById(R.id.thumbnail);
        }
    }

    @NonNull
    @Override
    public RecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        Context parentContext = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(parentContext);

        View videoView = inflater.inflate(R.layout.video_article_view, parent, false);

        ViewHolder holder = new ViewHolder(videoView);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        TextView textView = (TextView) holder.textView;
        TextView descriptionView = (TextView) holder.descriptionView;
        ImageView thumbnail = (ImageView) holder.imageView;

        Log.d(TAG, mLink.get(position));

        textView.setText(mLink.get(position));
        descriptionView.setText(mDescription.get(position));
        thumbnail.setImageBitmap(mThumbnails.get(position));
    }

    @Override
    public int getItemCount() {

        return mLink.size();
    }
}
