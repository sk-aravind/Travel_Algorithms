package com.skara.triggered.travelapp_triggered;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

/**
 * Created by weiqu on 11/14/2016.
 */
public class ItineraryFragment extends Fragment {

    public static final String ARG_PAGE = "ARG_PAGE";
    List<HomeScreen.Destination> dest_list;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        return inflater.inflate(R.layout.activity_itinerary,container,false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        initializeData();

        if (dest_list == null || dest_list.isEmpty() ) {
            displayText();
        }

        else{
            RecyclerView rv = (RecyclerView) getView().findViewById(R.id.rv);
            LinearLayoutManager llm = new LinearLayoutManager(getActivity());
            rv.setLayoutManager(llm);
            RVAdapterForIti adapter = new RVAdapterForIti(dest_list);
            rv.setAdapter(adapter);
        }
    }

    public static ItineraryFragment newInstance(int page) {
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, page);
        ItineraryFragment fragment = new ItineraryFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public void displayText(){
        LinearLayout linearLayout = (LinearLayout) getView().findViewById(R.id.info);

        TextView valueTV = new TextView(getActivity());
        valueTV.setText("You currently have no items in your itinerary");
        valueTV.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));

        linearLayout.addView(valueTV);
    }

    // Data ===============================================================================

    private void initializeData(){
        dest_list = HomeScreen.iti_list;
    }

    //===============================================================================

}
