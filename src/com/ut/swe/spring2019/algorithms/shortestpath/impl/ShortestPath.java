package com.ut.swe.spring2019.algorithms.shortestpath.impl;


import com.ut.swe.spring2019.algorithms.shortestpath.data.*;
import com.ut.swe.spring2019.algorithms.shortestpath.util.BaseData;
import com.ut.swe.spring2019.algorithms.shortestpath.util.Coordinate;
import com.ut.swe.spring2019.algorithms.shortestpath.util.Haversine;
import org.junit.jupiter.api.Test;

import java.util.*;

public class ShortestPath {

    private Graph graphObj;
    private BaseData dataObj;
    private Map<String, Vertex> airports;
    private Set<Flight> flights;

    public ShortestPath() {
        this.graphObj = new Graph();
        this.dataObj = BaseData.getInstance();
    }

    public boolean init() {
        boolean bRetVal = false;
        if (dataObj.initialize()) {
            this.airports = dataObj.getAirports();
            this.flights = dataObj.getFlights();
            bRetVal = true;
        } else {
            bRetVal = false;
        }
        return bRetVal;
    }

    public boolean buildGraph() {

        boolean bRetVal = false;

        if (this.init()) {
            Iterator<Flight> i = flights.iterator();
            while (i.hasNext()) {
                Flight currentFlight = i.next();
                try {
                    Vertex origin = airports.get(currentFlight.getOrigin());
                    Vertex destination = airports.get(currentFlight.getDestination());
                    if (origin != null && destination != null) {
                        graphObj.addUndirectNodes(origin, destination);
                    }
                } catch (NullPointerException e) {
                    e.printStackTrace();
                    continue;
                }
            }
            bRetVal = true;
        }
        return bRetVal;
    }

    public long calculateDistance(Vertex s, Vertex d){

        Coordinate source = new Coordinate(s.getLongitude(), s.getLatitude());
        Coordinate destination = new Coordinate(d.getLongitude(), d.getLatitude());
        return Math.round(Haversine.haversine(source, destination));
    }

    public void dijkstra(String source, String destination) {
        Route resultRoute = null;
        MinHeap minHeap = new MinHeap();
        Set<Vertex> visitedNodes = new HashSet<Vertex>();
        Set<Route> travelPath = new HashSet<Route>();
        Vertex startVertex = airports.get(source);
        Vertex endVertex = airports.get(destination);
        long distance = 0;

        List<Vertex> edges = graphObj.getEdges(airports.get(source));
        for (Vertex neighbor : edges) {
            distance = this.calculateDistance(airports.get(source),neighbor);
             minHeap.add(new Route(distance,source,neighbor.getCode()));
        }
        visitedNodes.add(startVertex); // Add starting (source node) to the visited set

        while(!minHeap.isEmpty()){
            Route shortestRoute = minHeap.poll();
            Vertex neighbor = airports.get(shortestRoute.getYetToVisitAirport());

            if (visitedNodes.contains(neighbor)){
                continue;
            }
            if (neighbor.equals(endVertex)) { // destination has been reached
                resultRoute = shortestRoute;
                break;
            }
            List<Vertex> further_edges = graphObj.getEdges(neighbor);
            for(Vertex further_neighbor : further_edges) {
                if(!visitedNodes.contains(further_neighbor)){

                    long new_distance = shortestRoute.getDistance() + this.calculateDistance(neighbor,further_neighbor);
                    Route newRoute = new Route();
                    newRoute.setDistance(new_distance);
                    for(int i=0; i< shortestRoute.getAirports().size();i++){
                        newRoute.addAirport(shortestRoute.getAirports().get(i)); // keep adding the airports in the route e..g SFO -> AUS -> DFW
                    }
                    newRoute.addAirport(further_neighbor.getCode());

                    minHeap.add(newRoute);
                }
            }
            visitedNodes.add(neighbor); // Add to visited node to the set
        }

        System.out.println("Distance between " + source + " and " + destination + " = " + resultRoute.getDistance() + " miles");
        System.out.println("Shortest route :");
        for(String code : resultRoute.getAirports()){
            System.out.println(airports.get(code).toString());
        }
    }

    public static void main(String[] args) {

        String source = "SFO";
        String destination = "CDG";
        ShortestPath objPath = new ShortestPath();
        if(objPath.buildGraph()){
            objPath.dijkstra(source,destination);
        }
    }

    @Test
    public void testGraph() {
        Graph g = new Graph();
        Vertex chennai = new Vertex("MAA", "Chennai International Airport", "India", 12.990005493164062, 80.16929626464844);
        Vertex mumbai = new Vertex("BOM", "Chhatrapati Shivaji International Airport", "India", 19.0886993408, 72.8678970337);
        Vertex goa = new Vertex("GOI", "Dabolim Airport", "India", 15.3808002472, 73.8313980103);
        Vertex nagpur = new Vertex("NAG", "Dr. Babasaheb Ambedkar International Airport", "India", 21.092199325561523, 79.04720306396484);
        Vertex trichy = new Vertex("TRZ", "Tiruchirapally Civil Airport Airport", "India", 10.765399932861328, 78.70970153808594);
        Vertex aus = new Vertex("AUS", "Austin International Airport", "USA", 10.765399932861328, 78.70970153808594);

        g.addVertex(chennai);
        g.addVertex(mumbai);
        g.addVertex(goa);
        g.addVertex(nagpur);
        g.addVertex(trichy);
        g.addVertex(aus);

        g.addEdge(chennai, mumbai);
        g.addEdge(chennai, goa);
        g.addEdge(mumbai, nagpur);
        g.addEdge(goa, nagpur);
        g.addEdge(chennai, trichy);

        System.out.println(g.toString());
    }

    @Test
    public void testHarversine() {
        String origin_airport = "BOM";
        String dest_airport = "MAA";
        if(this.buildGraph()){
            Coordinate source = new Coordinate(airports.get(origin_airport).getLongitude(), airports.get(origin_airport).getLatitude());
            Coordinate destination = new Coordinate(airports.get(dest_airport).getLongitude(), airports.get(dest_airport).getLatitude());
            System.out.println("Distance = " + Math.round(Haversine.haversine(source, destination)));
        }
    }

    @Test
    public void printGraph() {
        if(this.buildGraph()) {
            System.out.println(graphObj.toString());
        }
    }

    @Test void printGraphEdges(){

        String origin_airport = "DFW";
        if(this.buildGraph()) {
            List<Vertex> edges = graphObj.getEdges(airports.get(origin_airport));
            System.out.println("Edges of "+ origin_airport  + " : " + edges.size());
            for (Vertex v : edges) {
                System.out.println(v.toString());
            }
        }
    }
}
