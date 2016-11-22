
package com.skara.triggered.travelapp_triggered;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import java.util.List;



public class RVAdapter extends RecyclerView.Adapter<RVAdapter.ViewHolder> {

    List<HomeScreen.Destination> dest_list;

    private DetailsInterface detailsListener;

    RVAdapter(List<HomeScreen.Destination> dest_list,DetailsInterface detailsListener){
        this.dest_list = dest_list;
        this.detailsListener = detailsListener;
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        CardView cv;
        TextView destName;
        ImageView destPhoto;
        ImageButton addBtn;

        public ViewHolder(View itemView) {
            super(itemView);
            cv = (CardView)itemView.findViewById(R.id.cv);
            destName = (TextView)itemView.findViewById(R.id.dest_name);
            destPhoto = (ImageView)itemView.findViewById(R.id.dest_photo);
            addBtn = (ImageButton)itemView.findViewById(R.id.addBtn);

            addBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String dest = destName.getText().toString();
                    Toast.makeText(view.getContext(),"Added " + dest, Toast.LENGTH_SHORT).show();
                    addToItinerary(dest,(Integer) destPhoto.getTag(),getAdapterPosition());
                }
            });

            destPhoto.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    detailsListener.showDestDetails(view,destName.getText().toString(),(Integer) destPhoto.getTag());
                }
            });
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.destination_card, viewGroup, false);
        return new ViewHolder(v);
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

    public void addToItinerary(String destName,int destPhoto,int position){
        removeAt(position);
        for (HomeScreen.Destination j : HomeScreen.iti_list){
            if (j.name.equals(destName)){
                return;
            }
        }
        HomeScreen.iti_list.add(new HomeScreen.Destination(destName, destPhoto));
        HomeScreen.dest_list.remove(new HomeScreen.Destination(destName, destPhoto));
        HomeScreen.locationsToGo.add(TransportData.getLocationEnum(destName));
    }
}