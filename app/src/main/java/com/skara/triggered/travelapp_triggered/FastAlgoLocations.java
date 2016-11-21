package com.skara.triggered.travelapp_triggered;



import java.util.ArrayList;
import java.util.Arrays;


public class FastAlgoLocations {



    public static ArrayList<TransportData.locations> findShortestPath(ArrayList<TransportData.locations> locationsToGo, TransportData.locations startingLocation){

        ArrayList<TransportData.locations> shortestPath = new ArrayList<>();
        shortestPath.add(startingLocation);
        TransportData.locations location = startingLocation;
        ArrayList<TransportData.locations> ltg = new ArrayList<>();
        for (TransportData.locations l : locationsToGo){
            ltg.add(l);
        }
        //System.out.println("This is the starting location: " + location);
        while (ltg.size() >=1){
            location = shortestDist(ltg,location);
            //System.out.println("This is the next nearest neighbor: " + location);
            updateList(ltg, shortestPath, location);
            //System.out.println("Updating list now... This is the list so far..");
            //System.out.println(Arrays.toString(shortestPath.toArray()));
        }
        shortestPath.add(startingLocation);
        return shortestPath;
    }

    private static TransportData.locations shortestDist(ArrayList<TransportData.locations> ltg, TransportData.locations currentLocation) {
        double shortestDist = Integer.MAX_VALUE; //get distance for current location to first location in locationsToGo

        TransportData.locations closestLocation = null;

        for (TransportData.locations location: ltg){
            double shortDist = TransportData.getData(TransportData.transportType.WALK,currentLocation,location)[1];
            if (shortDist<shortestDist){
                shortestDist = shortDist;
                closestLocation = location;
            }
        }
        return closestLocation;

    }

    private static void updateList(ArrayList<TransportData.locations> ltg, ArrayList<TransportData.locations> shortestPath, TransportData.locations location){
        shortestPath.add(location);
        ltg.remove(location);
    }
}
