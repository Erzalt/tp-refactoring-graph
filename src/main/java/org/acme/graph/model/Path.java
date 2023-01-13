package org.acme.graph.model;

import java.util.ArrayList;
import java.util.List;

public class Path {


    private List<Edge> edges = new ArrayList<>();

    public Path(List<Edge> edges) {
        this.edges = edges;
    }
    public List<Edge> getEdges() {
        return edges;
    }
    public double getLenght(){
        double sum = 0;

        for( Edge edge:edges){
            double sourceX = edge.getSource().getCoordinate().getX();
            double sourceY = edge.getSource().getCoordinate().getY();
            double targetX = edge.getTarget().getCoordinate().getX();
            double targetY = edge.getTarget().getCoordinate().getY();
            sum += Math.sqrt((sourceY - targetY)*(sourceY - targetY) + (sourceX - targetX)*(sourceX - targetX));
        }
        return sum;
    }
}
