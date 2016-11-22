package com.skara.triggered.travelapp_triggered;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TravelRouteFragment extends Fragment {

    public static final String ARG_PAGE = "ARG_PAGE";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        return inflater.inflate(R.layout.activity_travel_route_fragment,container,false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        HomeScreen.algo_selected="";

        // Initialize Algorithm
        initializeData();

        if (HomeScreen.locationsToGo.isEmpty() || HomeScreen.locationsToGo == null) {
            displayText();
        }

        else {
            RecyclerView rv = (RecyclerView) getView().findViewById(R.id.rvTR);
            LinearLayoutManager llm = new LinearLayoutManager(getActivity());
            rv.setLayoutManager(llm);
            RVAdapterForTR adapter = new RVAdapterForTR(travel_list, totalCT_list);
            rv.setAdapter(adapter);
        }
    }

    public static TravelRouteFragment newInstance(int page) {
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, page);
        TravelRouteFragment fragment = new TravelRouteFragment();
        fragment.setArguments(args);
        return fragment;
    }


    public void displayText(){
        LinearLayout linearLayout = (LinearLayout) getView().findViewById(R.id.info);

        TextView valueTV = new TextView(getActivity());
        valueTV.setText(R.string.NoItems);
        valueTV.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));

        linearLayout.addView(valueTV);
    }

    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

    // Data ===============================================================================

    public static class TravelList{
        String path;
        String cost;
        String time;
        int photoId;

        TravelList(String path,String cost, String time, int photoId) {
            this.path = path;
            this.photoId = photoId;
            this.cost = cost;
            this.time = time;
        }
    }

    public static List<TravelList> travel_list;
    public static List<String> totalCT_list;

    private void initializeData() {
        if (HomeScreen.locationsToGo.isEmpty() || HomeScreen.locationsToGo == null){
            return;
        }
        //exhaustive algorithm is selected
        else if (HomeScreen.algo_selected.equals("Exhaustive")){
            travel_list = new ArrayList<>();
            totalCT_list = new ArrayList<>();
            ArrayList<String> ltg = new ArrayList<>();
            for (int j = 0; j<HomeScreen.locationsToGo.size(); j++ ) {
                String loc = HomeScreen.locationsToGo.get(j).toString();
                ltg.add(loc);
            }
            ArrayList output = ExhaustiveAlgo.optimal(ltg, (int)(HomeScreen.budget));
            double total_time = (double) output.get(0);
            double total_cost = (double) output.get(1);

            ArrayList transportSequence = (ArrayList) output.get(2);
            ArrayList locationSequence = (ArrayList) output.get(3);
            ArrayList transport_time = (ArrayList) output.get(4);
            ArrayList transport_cost = (ArrayList) output.get(5);

            for (int k = 0; k < transportSequence.size(); k++){
                String path = "From " + locationSequence.get(k) + " to " + locationSequence.get(k+1);
                String cost = "$" + transport_cost.get(k).toString();
                String time = transport_time.get(k).toString() +" mins";
                if (transportSequence.get(k).equals("By walk")){
                    travel_list.add(new TravelList(path,cost,time,R.drawable.walk));
                }
                else if (transportSequence.get(k).equals("By bus")){
                    travel_list.add(new TravelList(path,cost,time,R.drawable.bus));
                }
                else if (transportSequence.get(k).equals("By taxi")){
                    travel_list.add(new TravelList(path,cost,time,R.drawable.taxi));
                }
            }

            String totalCost = "Total Cost: $" + String.valueOf(round(total_cost,2));
            String totalTime = "Total Time: " + String.valueOf(round(total_time,2)) + " mins";
            totalCT_list.add(totalCost);
            totalCT_list.add(totalTime);

            return;
        }

        //default fast algo solver
        FastAlgo FastAlgoSolver = new FastAlgo(HomeScreen.budget, HomeScreen.startingLocation, HomeScreen.locationsToGo);

        travel_list = new ArrayList<>();
        totalCT_list = new ArrayList<>();

        List<String> transport_cost = new ArrayList<>();
        List<String> transport_time = new ArrayList<>();

        List<TransportData.locations> location_sequence = FastAlgoSolver.getLocationSequence();
        List<String> transport_sequence = Arrays.asList(FastAlgoSolver.getTransportSequence());
        double[] tc = FastAlgoSolver.getTransportCosts();
        double[] tt = FastAlgoSolver.getTransportTimes();
        for (double c : tc){
            String cs = String.valueOf(c);
            transport_cost.add(cs);
        }
        for (double t : tt){
            String ts = String.valueOf(t);
            transport_time.add(ts);
        }

        for (int i = 0; i<transport_sequence.size(); i++){
            String path = "From " + location_sequence.get(i).toString() + " to " + location_sequence.get(i+1).toString();
            String cost = "$" + transport_cost.get(i);
            String time = transport_time.get(i) +" mins";
            if (transport_sequence.get(i).equals("walk")){
                travel_list.add(new TravelList(path,cost,time,R.drawable.walk));
            }
            else if (transport_sequence.get(i).equals("bus")){
                travel_list.add(new TravelList(path,cost,time,R.drawable.bus));
            }
            else if (transport_sequence.get(i).equals("taxi")){
                travel_list.add(new TravelList(path,cost,time,R.drawable.taxi));
            }
        }
        String totalCost = "Total Cost: $" + String.valueOf(round(FastAlgoSolver.getTotalCost(),2));
        String totalTime = "Total Time: " + String.valueOf(round(FastAlgoSolver.getTotalTime(),2)) + " mins";
        totalCT_list.add(totalCost);
        totalCT_list.add(totalTime);
    }

    //===============================================================================

}
