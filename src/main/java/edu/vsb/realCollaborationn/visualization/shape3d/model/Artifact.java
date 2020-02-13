package edu.vsb.realCollaborationn.visualization.shape3d.model;


import javafx.scene.transform.Transform;

public interface Artifact {
int getArtifactPosition();
void setArtifactPosition(int newPosition);

void setArtifactKey(String artifactKey);
String getArtifactKey();
void addTransform(Transform transformer);

Transform getTransform();
}