package edu.vsb.realCollaborationn.visualization.shape3d.model;


import javafx.geometry.Point3D;
import javafx.scene.transform.Transform;

public interface Artifact {
void setArtifactPosition(int newPosition);
void setArtifactKey(String artifactKey);
void addOrSetTransform(Transform transformer);

Transform getTransform();
int getArtifactPositionInPartsSequence();
String getArtifactKey();
}