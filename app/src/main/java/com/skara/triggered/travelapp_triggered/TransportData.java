package com.skara.triggered.travelapp_triggered;

import java.util.HashMap;

public class TransportData {

    public enum transportType{
        TAXI,
        BUS,
        WALK,
    }
    public enum locations{
        MBS,
        SF,
        VC,
        RWS,
        BTRT,
        ZOO,
    }

    public static HashMap<locations, Integer> places;
    static{
        places = new HashMap<>();
        places.put(locations.MBS,0);
        places.put(locations.SF,1);
        places.put(locations.VC,2);
        places.put(locations.RWS,3);
        places.put(locations.BTRT,4);
        places.put(locations.ZOO,5);
    }

    public static double[][][] busT = {
            //To---->
            //(Cost (Dollars), Time (mins))
            // MBS SF VC RWS BTRT ZOO
            {{0.00, 0.00},{0.83,17},{1.18,26},{4.03,35},{0.88,19},{1.96,84}},
            {{0.83,17},{0.00,0.00},{1.26,31},{4.03,38},{0.98,24},{1.89,85}},
            {{1.18,24},{1.26,29},{0.00,0.00},{2.00,10.00},{0.98,18},{1.89,85}},
            {{1.18,33},{1.26,38},{0.00,10.00},{0.00,0.00},{0.98,27},{1.99,92}},
            {{0.88,18},{0.98,23},{0.98,19},{3.98,28},{0.00,0.00},{1.91,83}},
            {{1.88,86},{1.96,87},{2.11,86},{4.99,96},{1.91,84},{0.00,0.00}}
    };
    public static double[][][] walkT = {
            //To---->
            //(Cost (Dollars), Time (mins))
            // MBS SF VC RWS BTRT ZOO
            {{0.00, 0.00},{0.00,14},{0.00,69},{0.00,76},{0.00,28},{0.00,269}},
            {{0.00,14},{0.00,0.00},{0.00,81},{0.00,88},{0.00,39},{0.00,264}},
            {{0.00,69},{0.00,81},{0.00,0.00},{0.00,12},{0.00,47},{0.00,270.00}},
            {{0.00,76},{0.00,88},{0.00,12},{0.00,0.00},{0.00,55},{0.00,285}},
            {{0.00,28},{0.00,39},{0.00,47},{0.00,55},{0.00,0.00},{0.00,264}},
            {{0.00,269},{0.00,264},{0.00,270.00},{0.00,285},{0.00,264},{0.00,0.00}}
    };
    public static double[][][] taxiT = {
            //To---->
            //(Cost (Dollars), Time (mins))
            // MBS SF VC RWS BTRT ZOO
            {{0.00, 0.00},{3.22,3},{6.96,14},{8.5,19},{4.98,8},{18.4,30.00}},
            {{4.32,6},{0.00,0.00},{7.84,13},{9.38,18},{4.76,8},{18.18,29}},
            {{8.30,12},{7.96,14},{0.00,0.00},{4.54,9},{6.42,11},{22.58,31}},
            {{8.74,13},{8.4,14},{3.22,4},{0.00,0.00},{6.64,12},{22.8,32}},
            {{5.32,7},{4.76,8},{4.98,9},{6.52,14},{0.00,0.00},{18.40,30.00}},
            {{22.48,32},{19.40,29},{21.48,32},{23.68,36},{21.6,30.00},{0.00,0.00}}
    };


//    @param transportType enum
//    @return double[] {cost,time}


    public static double[] getData(transportType t, locations from, locations to) {
        int from_i = places.get(from);
        int to_i = places.get(to);
        double[] data = new double[2];
        if (t.equals(transportType.BUS)){
            data = busT[from_i][to_i];
        }
        if (t.equals(transportType.WALK)){
            data = walkT[from_i][to_i];
        }
        if (t.equals(transportType.TAXI)){
            data = taxiT[from_i][to_i];
        }
        return data;
    }

}
