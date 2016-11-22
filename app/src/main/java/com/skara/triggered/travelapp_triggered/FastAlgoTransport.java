package com.skara.triggered.travelapp_triggered;

import java.util.ArrayList;
import java.util.Random;


public class FastAlgoTransport {
    private ArrayList<Edge> edgeSequence;
    private double budget;

    private double solutionTime;
    private double solutionCost;
    private String[] solutionPath;

    public FastAlgoTransport(double budget,ArrayList<Edge> edgeSequence){
        checkEdgeSequence(edgeSequence);
        this.edgeSequence = edgeSequence;
        this.budget = budget;

    }

    public double getSolutionTime(){
        return solutionTime;
    }

    public double getSolutionCost(){
        return solutionCost;
    }

    public String[] getSolutionPath(){
        return solutionPath;
    }

    private void resetEdges(){
        for (Edge e: edgeSequence){
            e.reset();
        }
    }

    private void checkEdgeSequence(ArrayList<Edge> edgeSequence) {
        if (!edgeSequence.get(0).getLocationA().equals(edgeSequence.get(edgeSequence.size()-1).getLocationB())){
            throw new RuntimeException("Start and final location are not the same");
        }

        if (edgeSequence.size() ==1){
            throw new RuntimeException("edgeSequence must have more than 1 edge");
        }
    }

    public double getCurrentSum(){ // should be called only after solve
        double sum = 0;
        for(Edge e: edgeSequence){
            sum += e.getCurrentCost();
        }
        return sum;
    }

    public double getCurrentTime(){
        double time = 0;
        for(Edge e:edgeSequence){
            time +=e.getCurrentTime();
        }
        return time;
    }

    public String[] getCurrentPath(){ // should be called only after solve
        String[] solution = new String[edgeSequence.size()];
        int i = 0;
        for (Edge e: edgeSequence){
            solution[i] = e.getCurrentTransport();
            i++;
        }
        return solution;
    }

    public void solve(int numTrials){
        for (int i=0;i<numTrials;i++){
            this.solve();
            if (i==0){
                this.solutionCost = this.getCurrentSum();
                this.solutionTime = this.getCurrentTime();
                this.solutionPath = this.getCurrentPath();
            }
            else{
                if( getCurrentTime()< this.solutionTime){
                    this.solutionCost = this.getCurrentSum();
                    this.solutionTime = this.getCurrentTime();
                    this.solutionPath = this.getCurrentPath();
                }
            }
            this.resetEdges();
        }
    }

    public void solve(){
        this.resetEdges();
        Random randNumGen = new Random();
        //Base Case 1 : Budget accommodates all fastest transport

        if (getCurrentSum()<this.budget){
            return ;
        }
        for (int i = 0; i<2; i++){ //0->1 1->2
            //let i be the highest
            ArrayList<Integer> selectList = new ArrayList<>(); //index of edges with current edge index == i
            for (Edge e : edgeSequence){
                if (e.getCurrentIndex() == i){
                    selectList.add(edgeSequence.indexOf(e));
                }
            }

            while (!selectList.isEmpty()){
                int random_index = randNumGen.nextInt(selectList.size());
                int edgeSeqIndex = selectList.get(random_index);

                Edge edge = edgeSequence.get(edgeSeqIndex);
                edge.setNextAlternative();

                selectList.remove(random_index);

                if (getCurrentSum()<=this.budget){
                    return;
                }
            }

        }
        throw new RuntimeException("Budget can never be fulfilled, maybe walking is not free after all");
    }

}


