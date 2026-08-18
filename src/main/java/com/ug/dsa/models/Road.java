package com.ug.dsa.models;

public class Road {

    private int roadId;
    private int fromLocationId;
    private int toLocationId;
    private double distance;
    private double travelTime;
    private double roadConditionWeight;

    public Road() {
    }

    public Road(int roadId,
                int fromLocationId,
                int toLocationId,
                double distance,
                double travelTime,
                double roadConditionWeight) {
        this.roadId = roadId;
        this.fromLocationId = fromLocationId;
        this.toLocationId = toLocationId;
        this.distance = distance;
        this.travelTime = travelTime;
        this.roadConditionWeight = roadConditionWeight;
    }

    public int getRoadId() {
        return roadId;
    }

    public void setRoadId(int roadId) {
        this.roadId = roadId;
    }

    public int getFromLocationId() {
        return fromLocationId;
    }

    public void setFromLocationId(int fromLocationId) {
        this.fromLocationId = fromLocationId;
    }

    public int getToLocationId() {
        return toLocationId;
    }

    public void setToLocationId(int toLocationId) {
        this.toLocationId = toLocationId;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public double getTravelTime() {
        return travelTime;
    }

    public void setTravelTime(double travelTime) {
        this.travelTime = travelTime;
    }

    public double getRoadConditionWeight() {
        return roadConditionWeight;
    }

    public void setRoadConditionWeight(double roadConditionWeight) {
        this.roadConditionWeight = roadConditionWeight;
    }

    @Override
    public String toString() {
        return "Road{" +
                "roadId=" + roadId +
                ", fromLocationId=" + fromLocationId +
                ", toLocationId=" + toLocationId +
                ", distance=" + distance +
                ", travelTime=" + travelTime +
                ", roadConditionWeight=" + roadConditionWeight +
                '}';
    }
}