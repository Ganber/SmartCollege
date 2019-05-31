package com.example.smartcollege.Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.smartcollege.Activities.ShowEventActivity;
import com.example.smartcollege.R;

import java.util.ArrayList;

public class EventsRecyclerViewAdapter extends RecyclerView.Adapter<EventsRecyclerViewAdapter.ViewHolder>{

    private ArrayList<String> mIDs;
    private ArrayList<String> mDates;
    private int mImages;
    private Context mContext;

    //adapter that help to see the events
    public EventsRecyclerViewAdapter(ArrayList<String> mIDs, ArrayList<String> mDates, int mImages, Context mContext) {
        this.mIDs = mIDs;
        this.mDates = mDates;
        this.mContext = mContext;
        this.mImages = mImages;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.events_recycler_view_item, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        //set burglar's images in EventsActivity
        viewHolder.eventDate.setText(mDates.get(i));
        viewHolder.eventID.setText(mIDs.get(i));
        Glide.with(mContext)
                .load(mImages)
                .into(viewHolder.eventImage);

        viewHolder.parentLayout.setOnClickListener(v -> {
            Intent intent = new Intent(mContext, ShowEventActivity.class);
            intent.putExtra("ID", mIDs.get(i));
            intent.putExtra("Date", mDates.get(i));
            mContext.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return mIDs.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView eventID;
        TextView eventDate;
        ImageView eventImage;
        LinearLayout parentLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            eventDate = itemView.findViewById(R.id.textViewEventDate);
            eventID = itemView.findViewById(R.id.textViewEventID);
            eventImage = itemView.findViewById(R.id.imageViewEvents);
            parentLayout = itemView.findViewById(R.id.parent_layout);
        }
    }
}
