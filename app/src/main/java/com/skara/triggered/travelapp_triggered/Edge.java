package com.skara.triggered.travelapp_triggered;

import java.util.ArrayList;
import java.util.HashMap;

public class Edge{
    /** an Edge represents the transportation from LocationA -> LocationB
     *  it has a maximum of 3 states: TAXI,BUS,WALK
     * 	default starting state is the fastest transport but most expensive
     *	method setNextAlternative changes its state to the next cheaper alternative
     */
    public HashMap<String,double[]> tMap;
    private TransportData.locations locationA;
    private TransportData.locations locationB;
    private int currentIndex;  // 0- fastest/expensive  2 - slowest/free (2 must always be occupied)
    private String currentTransport;
    private ArrayList<String> ranked_list = new ArrayList<>(); //{0,1,2} 2 is always not empty
    /**
     * @param locationA and locationB are both Strings
     * 		  transport map is a hashmap with keys:
     * 		  walk bus taxi
     *  	  and associated value double[]->[cost,time]
     *  	  tmap should not be edited
     */
    public Edge(TransportData.locations locationA,TransportData.locations locationB){
        this.locationA = locationA;
        this.locationB = locationB;
        tMap = new HashMap<>();
        tMap.put("bus", TransportData.getData(TransportData.transportType.BUS, locationA, locationB));
        tMap.put("walk", TransportData.getData(TransportData.transportType.WALK, locationA, locationB));
        tMap.put("taxi", TransportData.getData(TransportData.transportType.TAXI, locationA, locationB));
        sortEdge();
    }

    public void reset(){
        this.currentTransport = "";
        for (String s: ranked_list){
            if ( currentTransport.equals("") & !s.equals("none")){
                currentTransport = s;
                currentIndex = ranked_list.indexOf(s);
            }
        }
    }

    private void sortEdge(){
        String[] transport = {"taxi","bus","walk"};
        while(ranked_list.size()!= transport.length){
            // rank_list contains transport with descending timing
            String nextFastestTransport = "";
            double nextFastestTiming = -1;
            for (String t : transport){
                if (!ranked_list.contains(t)){
                    if (nextFastestTiming ==-1){
                        nextFastestTiming = tMap.get(t)[1];
                        nextFastestTransport = t;
                    }
                    else if (tMap.get(t)[1]< nextFastestTiming){
                        nextFastestTiming = tMap.get(t)[1];
                        nextFastestTransport = t;
                    }
                }
            }
            ranked_list.add(nextFastestTransport);
        }

        //checking for and removing abnormalities -> transport is slower and more expensive
        for (int i = 0; i< 3; i++){
            for (int j = i+1; j<3; j++){
                // if A is faster than B and cheaper, change B to none
                if (!ranked_list.get(j).equals("none") && !ranked_list.get(i).equals("none")){
                    if (tMap.get(ranked_list.get(i))[0] < tMap.get(ranked_list.get(j))[0]){
                        ranked_list.set(j, "none");
                    }
                }
            }
        }

        // if last element is "none", replace last element with nearest mode of transport
        // so that if current_index == 2, there is no cheaper alternative
        if (ranked_list.get(ranked_list.size()-1).equals("none")){
            String last = "";
            for (String t: transport){
                if (!t.equals("none")){
                    last = t;
                }
            }
            int index_t = ranked_list.indexOf(last);
            ranked_list.set(index_t, "none");
            ranked_list.set(ranked_list.size()-1, last);

            if (last.equals("")){
                throw new RuntimeException("not supposed to reach here");
            }
        }
        // setting current index of edge
        currentTransport = "";
        for (String s: ranked_list){
            if ( currentTransport.equals("") & !s.equals("none")){
                currentTransport = s;
                currentIndex = ranked_list.indexOf(s);
            }
        }
    }
    public int getCurrentIndex(){
        return currentIndex;
    }
    public String getCurrentTransport(){
        return ranked_list.get(currentIndex);
    }
    public double getCurrentCost(){
        return tMap.get(ranked_list.get(currentIndex))[0];
    }
    public double getCurrentTime(){
        return tMap.get(ranked_list.get(currentIndex))[1];
    }
    public double getDistance(){
        return tMap.get("walk")[1];
    }
    public void setNextAlternative(){
        if (currentIndex == ranked_list.size()-1){
            throw new RuntimeException("no better alternative, check current index before invoking");
        }

        for (int i = currentIndex+1;i<ranked_list.size();i++){

            if (!ranked_list.get(i).equals("none")){
                this.currentIndex = i;
                return;
            }
        }
    }


    public String getLocationA(){
        return this.locationA.toString();
    }
    public String getLocationB(){
        return this.locationB.toString();
    }
}