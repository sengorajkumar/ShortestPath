package com.ut.swe.spring2019.algorithms.shortestpath.data;

import java.util.Objects;

public class Flight {
    private String origin;
    private String destination;
    private int nstops;

    public String getOrigin() {
        return origin;
    }

    public String getDestination() {
        return destination;
    }

    public int getNstops() {
        return nstops;
    }

    public Flight() {
        this.origin = null;
        this.destination = null;
        this.nstops = 0;
    }

    public Flight(String origin, String destination, int nstops) {
        this.origin = origin;
        this.destination = destination;
        this.nstops = nstops;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Flight flight = (Flight) o;
        //return nstops == flight.nstops &&
          //      Objects.equals(origin, flight.origin) &&
            //    Objects.equals(destination, flight.destination);
        return (nstops == flight.nstops && origin.equals(flight.origin) && destination.equals(flight.destination));
    }

    @Override
    public String toString() {
        return "Flight{" +
                "origin=" + origin +
                ", destination=" + destination +
                ", nstops=" + nstops +
                '}';
    }

    @Override
    public int hashCode() {
        return Objects.hash(origin, destination, nstops);
    }
}
