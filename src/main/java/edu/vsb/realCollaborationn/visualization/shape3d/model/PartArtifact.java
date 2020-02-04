package edu.vsb.realCollaborationn.visualization.shape3d.model;

import edu.vsb.realCollaborationn.visualization.importers.Importer3D;
import edu.vsb.realCollaborationn.visualization.utils3d.geom.transform.Affine3D;
import javafx.scene.Node;

import java.io.IOException;
import java.net.URL;

public class PartArtifact implements Artifact {

    private Affine3D artifactTransform;
    private String artifactKey;
    private int artifactPosition;
    Node artifactModel;

    public PartArtifact(URL artifactLoadPath, Affine3D artifactTransform, String artifactKey, int artifactPosition,
                        boolean loadAsPolyMesh) throws IOException {
        this.artifactTransform = artifactTransform;
        this.artifactKey = artifactKey;
        this.artifactPosition = artifactPosition;
        artifactModel = Importer3D.loadModelFile(artifactLoadPath.toString(), loadAsPolyMesh);
        artifactModel.getTransforms().add(artifactTransform.getTransform()); //fixme: check if the transform works
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

    @Override
    public void transform(Affine3D transformer) {
        artifactTransform.preTransform(transformer);
        artifactModel.getTransforms().add(artifactTransform.getTransform());
    }

    public Affine3D getArtifactTransform() {
        return artifactTransform;
    }

    public String getArtifactKey() {
        return artifactKey;
    }

}
