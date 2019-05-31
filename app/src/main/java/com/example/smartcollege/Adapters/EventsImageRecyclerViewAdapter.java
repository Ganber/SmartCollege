package com.example.smartcollege.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.smartcollege.R;

import java.util.ArrayList;

public class EventsImageRecyclerViewAdapter extends RecyclerView.Adapter<EventsImageRecyclerViewAdapter.ViewHolder>{

    private ArrayList<Integer> mImages;
    private Context mContext;
    //adapter that help to see the devices in DashboardActivity
    public EventsImageRecyclerViewAdapter(ArrayList<Integer> Images, Context Context) {
        this.mImages = Images;
        this.mContext = Context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.events_images_view_item, viewGroup, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        //set the images in activity
        Glide.with(mContext)
                .load(mImages.get(i))
                .into(viewHolder.eventImage);
    }

    @Override
    public int getItemCount() {
        return mImages.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView eventImage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            eventImage = itemView.findViewById(R.id.imageViewEventImage);
        }
    }
}
