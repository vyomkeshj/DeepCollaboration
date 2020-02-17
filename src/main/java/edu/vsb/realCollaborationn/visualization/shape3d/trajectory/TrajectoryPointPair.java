package edu.vsb.realCollaborationn.visualization.shape3d.trajectory;

import javafx.geometry.Point3D;

public class TrajectoryPointPair {
    Point3D fromPoint;
    Point3D toPoint;
    IntermediateTrajectoryType trajectoryType;

    public TrajectoryPointPair(Point3D fromPoint, Point3D toPoint, IntermediateTrajectoryType trajectoryType) {
        this.fromPoint = fromPoint;
        this.toPoint = toPoint;
        this.trajectoryType = trajectoryType;
    }

    public Point3D getFromPoint() {
        return fromPoint;
    }

    public Point3D getToPoint() {
        return toPoint;
    }

    public IntermediateTrajectoryType getTrajectoryType() {
        return trajectoryType;
    }
}
