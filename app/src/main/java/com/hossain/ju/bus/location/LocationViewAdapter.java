package com.hossain.ju.bus.location;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;
import com.hossain.ju.bus.R;
import java.util.List;

/**
 * Created by mohammod.hossain on 2/15/2018.
 */

public class LocationViewAdapter  extends RecyclerView.Adapter<LocationViewAdapter.TrackingViewHolder>{

    private Context mContext;
    private List<Location> trackingList;

    public LocationViewAdapter(Context context,List<Location> tracks){
        this.trackingList = tracks;
        this.mContext = context;
    }

    @Override
    public TrackingViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
         View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_tracking_row, viewGroup, false);

        return new TrackingViewHolder(v);
    }

    @Override
    public void onBindViewHolder(TrackingViewHolder holder, int position) {
        Location location = trackingList.get(position);
        holder.locId.setText(location.getLocId());
        holder.latitude.setText(location.getLatitude());
        holder.longitude.setText(location.getLongitude());
        holder.address.setText(location.getLocationName());
        holder.datetime.setText(location.getSaveTime());
    }

    @Override
    public int getItemCount() {
        return trackingList.size();
    }

    public static class TrackingViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView locId;
        TextView latitude;
        TextView longitude;
        TextView datetime;
        TextView address;



        TrackingViewHolder(View itemView) {
            super(itemView);


            locId           = (TextView)itemView.findViewById(R.id.txtSi);
            latitude     = (TextView)itemView.findViewById(R.id.txtLat);
            longitude     = (TextView)itemView.findViewById(R.id.txtLong);
            address         = (TextView)itemView.findViewById(R.id.txtAddress);
           datetime           = (TextView)itemView.findViewById(R.id.txtDateTime);


            itemView.setOnClickListener(this);
        }


        @Override
        public void onClick(View view) {
            TrackingViewHolder taskViewHolder = (TrackingViewHolder) view.getTag();
            int position = taskViewHolder.getAdapterPosition();

        }

    }
}


