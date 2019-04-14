package com.example.smartcollege;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class DeviceAdapter extends RecyclerView.Adapter<DeviceAdapter.MyViewHolder> {

    private String[] mDataset;

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public ImageView mDeviceImageView;
        public TextView mIDTextView;
        public TextView mNameTextView;
        public TextView mStatusTextView;

        public MyViewHolder(@NonNull View itemView, ImageView mDeviceImageView, TextView mIDTextView, TextView mNameTextView, TextView mStatusTextView) {
            super(itemView);
            this.mDeviceImageView = mDeviceImageView;
            this.mIDTextView = mIDTextView;
            this.mNameTextView = mNameTextView;
            this.mStatusTextView = mStatusTextView;
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public DeviceAdapter(String[] myDataset) {
        mDataset = myDataset;
    }

    @Override
    public DeviceAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//        TextView v = (TextView) LayoutInflater.from(parent.getContext())
//                .inflate(R.layout.device_recycle_view_item, parent, false);
//        MyViewHolder vh = new MyViewHolder(v);
//        return vh;
        return null;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.mIDTextView.setText(mDataset[position]);
        holder.mNameTextView.setText(mDataset[position]);
        holder.mStatusTextView.setText(mDataset[position]);
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.length;
    }
}
