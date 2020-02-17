package edu.vsb.realCollaborationn.visualization.shape3d.model;

import edu.vsb.realCollaborationn.visualization.importers.Importer3D;
import javafx.scene.Node;
import javafx.scene.transform.Transform;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class PartArtifact implements Artifact {

    private Transform artifactTransform;            //fixme: doesn't work yet
    private String artifactKey;
    private int artifactPosition;
    private Node artifactModel;
    private List<CollisionArtifact> partCollisionArtifact = new ArrayList<>();

    public PartArtifact(URL artifactLoadPath, String artifactKey, int artifactPosition,
                        boolean loadAsPolyMesh) throws IOException {
        this.artifactKey = artifactKey;
        this.artifactPosition = artifactPosition;

        artifactModel = Importer3D.loadModelFile(artifactLoadPath.toString(), loadAsPolyMesh);
        artifactModel.setId(artifactKey);
    }

    @Override
    public int getArtifactPositionInPartsSequence() {
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

    public String getArtifactKey() {
        return artifactKey;
    }

    @Override
    public void addOrSetTransform(Transform transformer) {
        artifactModel.getTransforms().add(transformer);
        for (CollisionArtifact curentColisionArtifact: partCollisionArtifact) {
            curentColisionArtifact.addOrSetTransform(transformer);
        }
    }

    @Override
    public Transform getTransform() {
        return artifactTransform;
    }

    public Node getArtifactModel() {
        return artifactModel;
    }

    public void addCollisionArtifact(CollisionArtifact collisionArtifact) {
        this.partCollisionArtifact.add(collisionArtifact);
    }
}
