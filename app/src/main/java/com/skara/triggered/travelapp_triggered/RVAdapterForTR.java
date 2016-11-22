package com.skara.triggered.travelapp_triggered;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.List;

public class RVAdapterForTR extends RecyclerView.Adapter<RVAdapterForTR.ViewHolder> {
    List<TravelRouteFragment.TravelList> travel_list;
    List<String> totalCT_list;

    public static final int TRAVELROUTE = 0;
    public static int[] dataSetTypes;

    RVAdapterForTR(List<TravelRouteFragment.TravelList> travel_list, List<String> totalCT_list){
        this.travel_list = travel_list;
        this.totalCT_list = totalCT_list;
        dataSetTypes = new int[travel_list.size()+1];
        for (int i = 0; i<travel_list.size();i++){
            dataSetTypes[i] = TRAVELROUTE;
        }
        dataSetTypes[travel_list.size()] = 1;
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(View itemView) {
            super(itemView);
        }
    }

    public class TravelRouteViewHolder extends ViewHolder{
        CardView cv;
        TextView path;
        TextView cost;
        TextView time;
        ImageView transport;

        public TravelRouteViewHolder(View itemView) {
            super(itemView);
            cv = (CardView)itemView.findViewById(R.id.cv);
            path = (TextView)itemView.findViewById(R.id.Path);
            cost = (TextView)itemView.findViewById(R.id.Cost);
            time = (TextView)itemView.findViewById(R.id.Time);
            transport = (ImageView)itemView.findViewById(R.id.Transport);

        }
    }

    public class TotalCostAndTimeViewHolder extends ViewHolder{
        CardView cv2;
        TextView totalCost;
        TextView totalTime;

        public TotalCostAndTimeViewHolder(View itemView) {
            super(itemView);
            cv2 = (CardView)itemView.findViewById(R.id.cv2);
            totalCost = (TextView)itemView.findViewById(R.id.TotalCost);
            totalTime = (TextView)itemView.findViewById(R.id.TotalTime);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        if (i == TRAVELROUTE ) {
            View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.travel_route_card, viewGroup, false);
            return new TravelRouteViewHolder(v);
        }
        else {
            View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.total_cost_and_time_card, viewGroup, false);
            return new TotalCostAndTimeViewHolder(v);
        }
    }

    @Override
    public void onBindViewHolder(ViewHolder ViewHolder, int i) {
        if (ViewHolder.getItemViewType() == TRAVELROUTE) {
            TravelRouteViewHolder holder = (TravelRouteViewHolder) ViewHolder;
            holder.path.setText(travel_list.get(i).path);
            holder.cost.setText(travel_list.get(i).cost);
            holder.time.setText(travel_list.get(i).time);
            holder.transport.setImageResource(travel_list.get(i).photoId);
        }
        else {
            TotalCostAndTimeViewHolder holder = (TotalCostAndTimeViewHolder) ViewHolder;
            holder.totalCost.setText(totalCT_list.get(0));
            holder.totalTime.setText(totalCT_list.get(1));
        }
    }

    @Override
    public int getItemCount() {
        return travel_list.size()+1;
    }

    @Override
    public int getItemViewType(int position){
        return dataSetTypes[position];
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

}