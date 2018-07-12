package com.hossain.ju.bus.route;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hossain.ju.bus.R;
import com.hossain.ju.bus.RecyclerViewClickListener;
import com.hossain.ju.bus.model.route.Route;

import java.util.List;
import java.util.Random;

/**
 * Created by mohammod.hossain on 2/15/2018.
 */

public class RouteViewAdapter extends RecyclerView.Adapter<RouteViewAdapter.RouteViewHolder> {

    static private Context mContext;
    private List<Route> routeList;
    public RecyclerViewClickListener recyclerViewClickListener;
    List<Integer> favList;
    int i = -1;
    String[] colorCode = {"#808000", "#1C7EDA", "#2C2FEC", "#00FF00", "#008000", "#17DC3E", "#A01FD1", "#BB1833", "#89CE1E", "#17DC3E"};
    private Random random = new Random();
    int c = 0;
    Boolean b;

    public RouteViewAdapter(Context context, List<Route> routes, List<Integer> favList, boolean b) {
        this.routeList = routes;
        this.mContext = context;
        this.favList = favList;
        this.b = b;
    }

    public void setFavList(List<Integer> favList) {
        this.favList = favList;
    }

    public void setRouteList(List<Route> routes) {
        this.routeList = routes;
    }

    @Override
    public RouteViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_route_row, viewGroup, false);
        return new RouteViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RouteViewHolder holder, int position) {
        Route route = routeList.get(position);
        if (!b) {
            holder.routeName.setText(route.getName());
            holder.routeDetails.setText(route.getSubtitle());
            Drawable drawable = holder.viewI.getBackground();
            int k = random.nextInt(10);
            while (i == k) {

                k = random.nextInt(10);
                // Log.d("TAGO", "k=" + k + "INNN");
            }
            drawable.setColorFilter(Color.parseColor(colorCode[k]), PorterDuff.Mode.MULTIPLY);
            i = k;
            //Log.d("TAGO", "k=" + k + "i=" + i);
            if (favList != null && favList.size() > 0 && favList.contains(route.getId())) {
                holder.imgFav.setImageResource(R.drawable.favorite_heart_button);
                holder.imgFav.setTag(1);
            } else {
                holder.imgFav.setTag(0);
                holder.imgFav.setImageResource(R.drawable.favorite);
            }
            Log.d("FFFFFFFF", c + "");
        } else {
            if (favList.contains(route.getId())) {
                holder.routeName.setText(route.getName());
                holder.routeDetails.setText(route.getSubtitle());
                Drawable drawable = holder.viewI.getBackground();
                int k = random.nextInt(10);
                while (i == k) {

                    k = random.nextInt(10);
                    // Log.d("TAGO", "k=" + k + "INNN");
                }
                drawable.setColorFilter(Color.parseColor(colorCode[k]), PorterDuff.Mode.MULTIPLY);
                i = k;
                holder.imgFav.setVisibility(View.GONE);
            } else {
                holder.linearLayout.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public int getItemCount() {
        return routeList.size();
    }

    public class RouteViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView routeName;
        TextView routeDetails;
        View viewI;
        ImageView imgLocation, imgFav;
        LinearLayout linearLayout;

        RouteViewHolder(View itemView) {
            super(itemView);
            routeName = (TextView) itemView.findViewById(R.id.txtRouteTitle);
            routeDetails = (TextView) itemView.findViewById(R.id.txtRouteDetails);
            viewI = (View) itemView.findViewById(R.id.viewI);
            linearLayout = (LinearLayout) itemView.findViewById(R.id.layoutRoot);
            imgLocation = (ImageView) itemView.findViewById(R.id.imgLocation);
            imgFav = (ImageView) itemView.findViewById(R.id.imgFav);
            itemView.setOnClickListener(this);
            imgLocation.setOnClickListener(this);
            imgFav.setOnClickListener(this);
            linearLayout.setOnClickListener(this);
        }


        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.imgFav:
                    RouteViewAdapter.this.recyclerViewClickListener.onClick(view, getAdapterPosition());
                    break;
                case R.id.imgLocation:
                    RouteViewAdapter.this.recyclerViewClickListener.onClick(view, getAdapterPosition());
                    break;
                case R.id.layoutRoot:
                    RouteViewAdapter.this.recyclerViewClickListener.onClick(view, getAdapterPosition());
                    break;
//                default:
//                    Intent intent = new Intent(mContext, MainActivity.class);
//                    intent.putExtra("id", routeList.get(getAdapterPosition()).getId());
//                    Log.d("TISS",routeList.get(getAdapterPosition()).getId()+"");
//                    mContext.startActivity(intent);
            }
        }
    }

    public void setOnClickListener(RecyclerViewClickListener recyclerViewClickListener) {
        this.recyclerViewClickListener = recyclerViewClickListener;
    }
}


