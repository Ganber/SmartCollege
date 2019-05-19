package com.example.smartcollege;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>{

    private ArrayList<String> mNames = new ArrayList<>();
    private ArrayList<Integer> mImages = new ArrayList<>();
    private ArrayList<Long> mIDs = new ArrayList<>();
    private ArrayList<String> mStatus = new ArrayList<>();
    private ArrayList<String> mRoom = new ArrayList<>();
    private ArrayList<String> mType = new ArrayList<>();
    private ArrayList<String> mIsActive = new ArrayList<>();
    private Context mContext;

    public RecyclerViewAdapter(ArrayList<String> mNames, ArrayList<Integer> mImages, ArrayList<Long> mIDs, ArrayList<String> mStatus, ArrayList<String> mRoom, ArrayList<String> mType, ArrayList<String> mIsActive, Context mContext) {
        this.mNames = mNames;
        this.mImages = mImages;
        this.mIDs = mIDs;
        this.mStatus = mStatus;
        this.mRoom = mRoom;
        this.mType = mType;
        this.mIsActive = mIsActive;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.device_recycle_view_item, viewGroup, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {

//        Glide.with(mContext)
//                .asBitmap()
//                .load(mImages.get(i))
//                .into(viewHolder.image);
        Glide.with(mContext)
                .load(mImages.get(i))
                .into(viewHolder.image);

        viewHolder.name.setText(mNames.get(i));
        viewHolder.ID.setText(mIDs.get(i).toString());
        viewHolder.status.setText(mStatus.get(i));
        viewHolder.room.setText(mRoom.get(i));
        viewHolder.type.setText(mType.get(i));
        viewHolder.isActive.setText(mIsActive.get(i));


        CharSequence txt = viewHolder.isActive.getText();
        if (txt == "ACTIVE") {
            viewHolder.isActive.setTextColor(Color.GREEN);
        }else {
            viewHolder.isActive.setTextColor(Color.RED);
        }
    }

    @Override
    public int getItemCount() {
        return mNames.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView image;
        TextView ID;
        TextView name;
        TextView status;
        TextView room;
        TextView type;
        TextView isActive;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            image = itemView.findViewById(R.id.imageViewDevice);
            ID = itemView.findViewById(R.id.textViewID);
            name = itemView.findViewById(R.id.textViewName);
            status = itemView.findViewById(R.id.textViewStatus);
            room = itemView.findViewById(R.id.textViewRoom);
            type = itemView.findViewById(R.id.textViewType);
            isActive = itemView.findViewById(R.id.textViewIsActive);
        }
    }
}
