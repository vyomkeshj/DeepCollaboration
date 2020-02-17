package edu.vsb.realCollaborationn.visualization.shape3d.model;

import javafx.geometry.Point3D;
import javafx.scene.Node;
import javafx.scene.transform.Transform;

public interface CollisionArtifact {
    double getDistanceFromCollisionArtifact(Point3D otherPoint);
    void setArtifactCollisionRadius(double radius);
    void addOrSetTransform(Transform transformer);
    Node getCorrespondingNode();
}
