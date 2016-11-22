package com.skara.triggered.travelapp_triggered;

import java.util.ArrayList;

public class FastAlgo {
    public ArrayList<TransportData.locations> locationSequence;
    private FastAlgoTransport solver;
    private final int numTrials =100;
    private String[] solutionPath;
    private double solutionCost;
    private double solutionTime;
    private double[] timePath;
    private double[] costPath;
    private double budget;

    public FastAlgo(double budget, TransportData.locations start, ArrayList<TransportData.locations> locationsToGo){
        this.budget = budget;

        //solve location sequence
        locationSequence = FastAlgoLocations.findShortestPath(locationsToGo,start);

        //solve transportation
        ArrayList<Edge> edgeSequence = new ArrayList<>();
        for (int i = 0; i< locationSequence.size()-1;i++){
            TransportData.locations fromLocation = locationSequence.get(i);
            TransportData.locations toLocation = locationSequence.get(i+1);
            edgeSequence.add(new Edge(fromLocation,toLocation));

        }
        this.solver = new FastAlgoTransport(budget,edgeSequence);
        solver.solve(numTrials);
        this.solutionPath = solver.getSolutionPath();
        this.solutionCost = solver.getSolutionCost();
        this.solutionTime = solver.getSolutionTime();


        timePath = new double[this.solutionPath.length];
        costPath = new double[this.solutionPath.length];

        for(int i = 0; i < this.solutionPath.length; i++){
            TransportData.transportType transport = TransportData.getTransportType(this.solutionPath[i]);
            TransportData.locations locationA = this.locationSequence.get(i);
            TransportData.locations locationB = this.locationSequence.get(i+1);
            double[] data = TransportData.getData(transport, locationA, locationB);
            costPath[i] = data[0];
            timePath[i] = data[1];
        }
    }
    public double getBudget(){
        return this.budget;
    }

    public String[] getTransportSequence(){
        return this.solutionPath;
    }

    public double getTotalCost(){
        return this.solutionCost;
    }

    public double getTotalTime(){
        return this.solutionTime;
    }

    public double[] getTransportTimes(){
        return this.timePath;
    }
    public double[] getTransportCosts(){
        return this.costPath;
    }
    public ArrayList<TransportData.locations> getLocationSequence(){
        return this.locationSequence;
    }

}