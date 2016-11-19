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

import java.util.ArrayList;
import java.util.List;

/**
 * Created by weiqu on 11/14/2016.
 */
public class TravelRouteFragment extends Fragment {

    public static final String ARG_PAGE = "ARG_PAGE";
    List<HomeScreen.Destination> dest_list;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        return inflater.inflate(R.layout.activity_travel_route_fragment,container,false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        initializeData();

        RecyclerView rv = (RecyclerView) getView().findViewById(R.id.rv);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        rv.setLayoutManager(llm);
        RVAdapterForTR adapter = new RVAdapterForTR(dest_list);
        rv.setAdapter(adapter);
    }

    public static TravelRouteFragment newInstance(int page) {
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, page);
        TravelRouteFragment fragment = new TravelRouteFragment();
        fragment.setArguments(args);
        return fragment;
    }


    // Data ===============================================================================


    private List<HomeScreen.Destination> travel_list;

    private void initializeData() {
        travel_list = new ArrayList<>();
        travel_list.add(new HomeScreen.Destination("BY FOOT", R.drawable.walk));
        travel_list.add(new HomeScreen.Destination("BY BUS", R.drawable.bus));
        travel_list.add(new HomeScreen.Destination("BY TAXI", R.drawable.taxi));
        dest_list = travel_list;
    }

    //===============================================================================

}
