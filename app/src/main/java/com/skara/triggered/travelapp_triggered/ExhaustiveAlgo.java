package com.skara.triggered.travelapp_triggered;

import java.util.ArrayList;
import java.util.HashMap;

public class ExhaustiveAlgo {

    public enum transportType{
        TAXI,
        BUS,
        WALK,
    }

    //Maps char to integer for lookup
    public static HashMap<Character, Integer> places;
    static{
        places = new HashMap<>();
        places.put('a',0);  //MBS
        places.put('b',1);  //SF
        places.put('c',2);  //VC
        places.put('d',3);  //RWS
        places.put('e',4);  //BTRT
        places.put('f',5);  //ZOO
    }

    //Transport data arrays
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


    // gets cost of trip between two locations
    public static double getCost(transportType t, char from, char to) {

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
        return data[0];
    }

    // gets time of trip between two locations
    public static double getTime(transportType t, char from, char to) {

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
        return data[1];
    }

    //Maps location to a char value
    public static HashMap<String, Character> locationMap;
    static{
        locationMap = new HashMap<>();
        locationMap.put("MBS",'a');
        locationMap.put("SF",'b');
        locationMap.put("VC",'c');
        locationMap.put("RWS",'d');
        locationMap.put("BTRT",'e');
        locationMap.put("ZOO",'f');
    }


    //Creates string of chars FROM input locations
    private static String charLocations (ArrayList<String> locations) {
        String charHolder = "";
        for (String s: locations) {
            char c = locationMap.get(s);
            charHolder=charHolder+c;
        }
        return charHolder;

    }

    //Maps chars to location
    public static HashMap<Character,String> charMap;
    static{
        charMap = new HashMap<>();
        charMap.put('a',"MBS");
        charMap.put('b',"SF");
        charMap.put('c',"VC");
        charMap.put('d',"RWS");
        charMap.put('e',"BTRT");
        charMap.put('f',"ZOO");
    }

    //Convert string of chars TO locations
    private static ArrayList<String>  locationChar (String s){
        ArrayList<String> output = new ArrayList<>();
        char[] charArray = s.toCharArray();
        for (char c: charArray){
            String str = charMap.get(c);
            output.add(str);

        }
        return output;
    }

    //Permute string of char locations and appends hotel
    public static void permuteLocations (ArrayList<String> result, String prefix, String s, String suffix){
        int len = s.length();
        if(len==0){
            result.add(prefix+suffix);
        } else {
            for(int i=0; i<len; i++){
                permuteLocations(result, prefix+s.charAt(i),s.substring(0,i) + s.substring(i+1,len),suffix);
            }
        }
    }

    public static ArrayList bestSubroute (String subroute, ArrayList<String> mode,
                                          double budget, double cost,
                                          double time, ArrayList<Double> timeData, ArrayList<Double> costData,
                                          ArrayList result){


        int s = subroute.length();


        if(s==1){

            if( time < (double) result.get(0) && cost <=budget){

                result.clear();
                result.add(time);
                result.add(cost);
                ArrayList<String> tempMode = new ArrayList<>(mode);
                ArrayList<Double> tempTimeData = new ArrayList<>(timeData);
                ArrayList<Double> tempCostData = new ArrayList<>(costData);
                result.add(tempMode);
                result.add(tempTimeData);
                result.add(tempCostData);




            }} else {


            mode.add("By walk");
            char from = subroute.charAt(0);
            char to = subroute.charAt(1);
            String tempSubroute = subroute.substring(1,subroute.length());
            double tempTime = time + getTime(transportType.WALK,from,to);
            double tempCost = cost + getCost(transportType.WALK,from,to);
            timeData.add(getTime(transportType.WALK,from,to));
            costData.add(getCost(transportType.WALK,from,to));

            result = bestSubroute(tempSubroute,mode,budget,tempCost,tempTime,timeData,costData,result);
            mode.remove(mode.size()-1);
            timeData.remove(timeData.size()-1);
            costData.remove(costData.size()-1);

            //BUS
            mode.add("By bus");
            tempTime = time + getTime(transportType.BUS,from,to);
            tempCost = cost + getCost(transportType.BUS,from,to);
            timeData.add(getTime(transportType.BUS,from,to));
            costData.add(getCost(transportType.BUS,from,to));
            result = bestSubroute(tempSubroute,mode,budget,tempCost,tempTime,timeData,costData,result);
            mode.remove(mode.size()-1);
            timeData.remove(timeData.size()-1);
            costData.remove(costData.size()-1);

            //TAXI
            mode.add("By taxi");
            tempTime = time + getTime(transportType.TAXI,from,to);
            tempCost = cost + getCost(transportType.TAXI,from,to);
            timeData.add(getTime(transportType.TAXI,from,to));
            costData.add(getCost(transportType.TAXI,from,to));
            result = bestSubroute(tempSubroute,mode,budget,tempCost,tempTime,timeData,costData,result);

            mode.remove(mode.size()-1);
            timeData.remove(timeData.size()-1);
            costData.remove(costData.size()-1);


        }
        return result;
    }


    public static ArrayList optimal (ArrayList<String> locations, int budget){
        String s = charLocations(locations);
        ArrayList<String> permuted = new ArrayList<>();
        permuteLocations(permuted,"a",s,"a");
        ArrayList bestRoute=new ArrayList();
        bestRoute.add(1000.0);

        for (int i=0;i<permuted.size();i++){
            ArrayList initRoute = new ArrayList();
            initRoute.clear();
            initRoute.add(1000.0);
            ArrayList localBestRoute = bestSubroute(permuted.get(i), new ArrayList<String>(),budget,0,0,new ArrayList<Double>(),new ArrayList<Double>(),initRoute);
            if((double) localBestRoute.get(0)< (double) bestRoute.get(0)){
                bestRoute.clear();
                ArrayList<String> route = locationChar(permuted.get(i));
                bestRoute.add(localBestRoute.get(0));
                bestRoute.add(localBestRoute.get(1));
                bestRoute.add(localBestRoute.get(2));
                bestRoute.add(route);
                bestRoute.add(localBestRoute.get(3));
                bestRoute.add(localBestRoute.get(4));


            }
        }
        return bestRoute;
    }

}
