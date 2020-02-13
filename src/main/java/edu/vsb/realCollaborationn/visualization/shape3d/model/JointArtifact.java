package edu.vsb.realCollaborationn.visualization.shape3d.model;

import javafx.scene.transform.Rotate;
import javafx.scene.transform.Transform;

public class JointArtifact implements Artifact{
    private Rotate jointRotation;
    private String artifactKey;
    private int artifactPosition;
    private int jointMinimumAngle;
    private int jointMaximumAngle;
    private double jointCurrentAngle;

    public JointArtifact(Rotate jointRotation, String artifactKey, int artifactPosition, int jointMinimumAngle, int jointMaximumAngle, double jointCurrentAngle) {
        this.jointRotation = jointRotation;
        this.artifactKey = artifactKey;
        this.artifactPosition = artifactPosition;
        this.jointMinimumAngle = jointMinimumAngle;
        this.jointMaximumAngle = jointMaximumAngle;
        this.jointCurrentAngle = jointCurrentAngle;
    }

    public Rotate getJointAxis() {
        return jointRotation;
    }

    public void setJointRotation(Rotate jointRotation) {
        this.jointRotation = jointRotation;
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
    public void addTransform(Transform transformer) {

    }

    @Override
    public Transform getTransform() {
        return jointRotation;    //todo: return a real transform
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

    public double getJointCurrentAngle() {
        return jointCurrentAngle;
    }

    public void setJointCurrentAngle(double jointCurrentAngle) {
        this.jointCurrentAngle = jointCurrentAngle;
    }
}
