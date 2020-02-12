package edu.vsb.realCollaborationn.visualization.shape3d.model;


import edu.vsb.realCollaborationn.visualization.utils3d.geom.transform.Affine3D;

public interface Artifact {
int getArtifactPosition();
void setArtifactPosition(int newPosition);

void setArtifactKey(String artifactKey);
String getArtifactKey();
void setTransform(Affine3D transformer);

Affine3D getTransform();
}