package com.skara.triggered.travelapp_triggered;

import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

/**
 * Created by Skara on 9/11/16.
 */

public class RVAdapterForIti extends RecyclerView.Adapter<RVAdapterForIti.ViewHolder> {
    //all the methods needs to be overwritten to prevent error
    List<HomeScreen.Destination> dest_list;

    RVAdapterForIti(List<HomeScreen.Destination> dest_list){
        this.dest_list = dest_list;
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        CardView cv;
        TextView destName;
        ImageView destPhoto;
        FloatingActionButton removeBtn;

        public ViewHolder(View itemView) {
            super(itemView);
            cv = (CardView)itemView.findViewById(R.id.cv);
            destName = (TextView)itemView.findViewById(R.id.dest_name);
            destPhoto = (ImageView)itemView.findViewById(R.id.dest_photo);
            removeBtn = (FloatingActionButton)itemView.findViewById(R.id.removeBtn);

            removeBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String dest = destName.getText().toString();
                    Toast.makeText(view.getContext(),"Removed " + dest, Toast.LENGTH_SHORT).show();
                    removeFromItinerary(destName.getText().toString(),(Integer) destPhoto.getTag(),getAdapterPosition());
                }
            });
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.itinerary_card, viewGroup, false);
        ViewHolder pvh = new ViewHolder(v);
        return pvh;
    }

    @Override
    public void onBindViewHolder(ViewHolder ViewHolder, int i) {
        ViewHolder.destName.setText(dest_list.get(i).name);
        ViewHolder.destPhoto.setImageResource(dest_list.get(i).photoId);
        ViewHolder.destPhoto.setTag(dest_list.get(i).photoId);
    }

    @Override
    public int getItemCount() {
        return dest_list.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    public void removeAt(int position){
        dest_list.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, dest_list.size());
    }

    public void removeFromItinerary(String s,int i,int position){
        removeAt(position);
        HomeScreen.iti_list.remove(new HomeScreen.Destination(s, i));
    }
}