package edu.vsb.realCollaborationn.visualization.shape3d.model;

import edu.vsb.realCollaborationn.visualization.utils3d.geom.transform.Affine3D;

public class JointArtifact implements Artifact{
    private Affine3D jointAxis;
    private String artifactKey;
    private int artifactPosition;
    private int jointMinimumAngle;
    private int jointMaximumAngle;
    private int jointCurrentAngle;

    public Affine3D getJointAxis() {
        return jointAxis;
    }

    public void setJointAxis(Affine3D jointAxis) {
        this.jointAxis = jointAxis;
    }

    @Override
    public int getArtifactPosition() {
        return artifactPosition;
    }

    @Override
    public void setArtifactPosition(int newPosition) {
        this.artifactPosition = newPosition;
    }

    @Override
    public void setArtifactKey(String artifactKey) {
        this.artifactKey = artifactKey;
    }

    @Override
    public void transform(Affine3D transformer) {
        jointAxis.preTransform(transformer);
    }

    public Affine3D getTransformerForRotation(double rotationAngle) {
        Affine3D rotationTransform = jointAxis;
        rotationTransform.rotate(rotationAngle);
        return rotationTransform;
    }

    public String getArtifactKey() {
        return artifactKey;
    }

    public int getJointMinimumAngle() {
        return jointMinimumAngle;
    }

    public void setJointMinimumAngle(int jointMinimumAngle) {
        this.jointMinimumAngle = jointMinimumAngle;
    }

    public int getJointMaximumAngle() {
        return jointMaximumAngle;
    }

    public void setJointMaximumAngle(int jointMaximumAngle) {
        this.jointMaximumAngle = jointMaximumAngle;
    }

    public int getJointCurrentAngle() {
        return jointCurrentAngle;
    }

    public void setJointCurrentAngle(int jointCurrentAngle) {
        this.jointCurrentAngle = jointCurrentAngle;
    }
}
