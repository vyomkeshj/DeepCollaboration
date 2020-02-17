package edu.vsb.realCollaborationn.visualization.shape3d.trajectory;

import javafx.geometry.Point3D;

import java.util.List;

public interface IntermediateTrajectoryType {
    List<Point3D> getPointsToBeTraversed(Point3D pointFrom, Point3D pointTo, float stepResolution);
}
