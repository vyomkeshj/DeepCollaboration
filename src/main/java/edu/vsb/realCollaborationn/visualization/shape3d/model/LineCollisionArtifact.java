package edu.vsb.realCollaborationn.visualization.shape3d.model;

import javafx.geometry.Point3D;
import javafx.scene.Node;
import javafx.scene.shape.Cylinder;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Transform;
import javafx.scene.transform.Translate;

public class LineCollisionArtifact implements CollisionArtifact {

    Point3D lineFrom;
    Point3D lineTo;
    double collisionRadius = 0.010;
    Node lineNode;

    public LineCollisionArtifact(Point3D lineFrom, Point3D lineTo, double collisionRadius) {
        this.lineFrom = lineFrom;
        this.lineTo = lineTo;
        this.collisionRadius = collisionRadius;
        lineNode = createLine(lineFrom, lineTo);
    }

    @Override
    public double getDistanceFromCollisionArtifact(Point3D otherPoint) {
        return computeDistBetweenPointAndLine(otherPoint, this.lineFrom, this.lineTo);
    }

    @Override
    public void setArtifactCollisionRadius(double radius) {
        this.collisionRadius = collisionRadius;
    }

    @Override
    public Node getCorrespondingNode() {
        return lineNode;
    }

    private Cylinder createLine(Point3D origin, Point3D target) {
        Point3D yAxis = new Point3D(0, 1, 0);
        Point3D diff = target.subtract(origin);
        double height = diff.magnitude();

        Point3D mid = target.midpoint(origin);
        Translate moveToMidpoint = new Translate(mid.getX(), mid.getY(), mid.getZ());

        Point3D axisOfRotation = diff.crossProduct(yAxis);
        double angle = Math.acos(diff.normalize().dotProduct(yAxis));
        Rotate rotateAroundCenter = new Rotate(-Math.toDegrees(angle), axisOfRotation);
        Cylinder line = new Cylinder(0.001, height);
        line.getTransforms().addAll(moveToMidpoint, rotateAroundCenter);

        return line;
    }

    @Override
    public void addOrSetTransform(Transform transformer) {
        lineNode.getTransforms().add(transformer);
        lineFrom = lineNode.getLocalToParentTransform().transform(lineFrom);
        lineTo = lineNode.getLocalToParentTransform().transform(lineTo);            //todo: test this
    }

    private float computeDistBetweenPointAndLine(Point3D point, Point3D lineFrom, Point3D lineTo){
        Point3D PointThing = new Point3D(
                 lineFrom.getX() - point.getX(),
                lineFrom.getY() - point.getY(),
                lineFrom.getZ() - point.getZ());
        Point3D TotalThing = new Point3D((PointThing.getY()*lineTo.getZ() - PointThing.getZ()*lineTo.getY()),
                -(PointThing.getX()*lineTo.getZ() - PointThing.getZ()*lineTo.getX()),
                (PointThing.getX()*lineTo.getY() - PointThing.getY()*lineTo.getX())
        );
        float distance = (float) (Math.sqrt(TotalThing.getX()*TotalThing.getX() + TotalThing.getY()*TotalThing.getY() + TotalThing.getZ()*TotalThing.getZ()) /
                Math.sqrt(lineTo.getX() * lineTo.getX() + lineTo.getY() * lineTo.getY() + lineTo.getZ() * lineTo.getZ() ));
        return distance;
    }
}
