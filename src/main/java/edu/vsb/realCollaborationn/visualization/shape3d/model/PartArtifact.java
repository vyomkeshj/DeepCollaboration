package edu.vsb.realCollaborationn.visualization.shape3d.model;

import edu.vsb.realCollaborationn.visualization.importers.Importer3D;
import javafx.scene.Node;
import javafx.scene.transform.Affine;
import javafx.scene.transform.Transform;

import java.io.IOException;
import java.net.URL;

public class PartArtifact implements Artifact {

    private Transform artifactTransform;

    private String artifactKey;
    private int artifactPosition;
    Node artifactModel;

    public PartArtifact(URL artifactLoadPath, String artifactKey, int artifactPosition,
                        boolean loadAsPolyMesh) throws IOException {
        this.artifactKey = artifactKey;
        this.artifactPosition = artifactPosition;

        artifactModel = Importer3D.loadModelFile(artifactLoadPath.toString(), loadAsPolyMesh);
        artifactModel.setId(artifactKey);
    }

    @Override
    public int getArtifactPosition() {
        return artifactPosition;
    }

    @Override
    public void setArtifactPosition(int newPosition) {
        this.artifactPosition = artifactPosition;
    }

    @Override
    public void setArtifactKey(String artifactKey) {
        this.artifactKey = artifactKey;
    }


    public Transform getArtifactTransform() {
        return artifactTransform;
    }

    public void setArtifactTransform(Transform artifactTransform) {
        this.artifactTransform = artifactTransform;
    }

    public String getArtifactKey() {
        return artifactKey;
    }

    @Override
    public void addTransform(Transform transformer) {
        artifactModel.getTransforms().add(transformer);
    }

    @Override
    public Transform getTransform() {
        return artifactTransform;
    }

    public Node getArtifactModel() {
        return artifactModel;
    }
}
