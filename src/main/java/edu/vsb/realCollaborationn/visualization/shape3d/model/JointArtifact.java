package edu.vsb.realCollaborationn.visualization.shape3d.model;

import edu.vsb.realCollaborationn.visualization.utils3d.geom.Vec3d;
import edu.vsb.realCollaborationn.visualization.utils3d.geom.transform.Affine3D;

public class JointArtifact implements Artifact{
    private Vec3d rotationAxis;
    private Vec3d jointTranslation;
    private String artifactKey;
    private int artifactPosition;
    private int jointMinimumAngle;
    private int jointMaximumAngle;
    private double jointCurrentAngle;

    public JointArtifact(Vec3d jointAxis, Vec3d jointTranslation, String artifactKey, int artifactPosition, int jointMinimumAngle, int jointMaximumAngle, double jointCurrentAngle) {
        this.rotationAxis = jointAxis;
        this.jointTranslation = jointTranslation;
        this.artifactKey = artifactKey;
        this.artifactPosition = artifactPosition;
        this.jointMinimumAngle = jointMinimumAngle;
        this.jointMaximumAngle = jointMaximumAngle;
        this.jointCurrentAngle = jointCurrentAngle;
    }

    public Vec3d getJointAxis() {
        return rotationAxis;
    }

    public Vec3d getJointTranslation() {
        return jointTranslation;
    }

    public void setJointAxis(Vec3d jointAxis) {
        this.rotationAxis = jointAxis;
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
    public void setTransform(Affine3D transformer) {

    }

    @Override
    public Affine3D getTransform() {
        Affine3D currentJointTransform = new Affine3D();
        currentJointTransform.setToRotation(jointCurrentAngle, rotationAxis.x, rotationAxis.y, rotationAxis.z,
                jointTranslation.x, jointTranslation.y, jointTranslation.z);
        return currentJointTransform;    //todo: return a real transform
    }

    public Affine3D getTransformerForRotation(double rotationAngle) {
        if(rotationAngle<=jointMaximumAngle && rotationAngle >= jointMinimumAngle) {
            rotationAngle = rotationAngle - jointCurrentAngle;

            Affine3D returnAffine = new Affine3D();

            returnAffine.setToRotation(rotationAngle, rotationAxis.x, rotationAxis.y, rotationAxis.z,
                    jointTranslation.x, jointTranslation.y, jointTranslation.z);
            return returnAffine;
        }
       return null;
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
