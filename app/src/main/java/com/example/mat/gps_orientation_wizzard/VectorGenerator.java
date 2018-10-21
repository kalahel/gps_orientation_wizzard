package com.example.mat.gps_orientation_wizzard;

public class VectorGenerator {
    public static double[] computeVector(double originLongitude, double originLatitude, double waypointLongitude, double waypointLatitude){
        double[] resultVector;
        resultVector = new double[2];
        resultVector[0] = waypointLongitude - originLongitude;
        resultVector[1] = waypointLatitude - originLatitude;
        return resultVector;
    }
}
