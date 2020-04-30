package edu.vsb.realCollaborationn.visualization.robot;

import edu.vsb.realCollaborationn.visualization.shape3d.model.ArtifactStructure;
import edu.vsb.realCollaborationn.visualization.shape3d.model.JointArtifact;
import edu.vsb.realCollaborationn.visualization.shape3d.model.PartArtifact;
import edu.vsb.realCollaborationn.visualization.utils3d.geom.transform.Affine3D;
import javafx.geometry.Point3D;
import javafx.scene.transform.Rotate;

import java.io.IOException;
import java.net.URL;

public class UR3Model extends ArtifactStructure {

    private static final double PI = 3.14;
    private final Rotate baseShoulderJointTransformation = new Rotate(0,0, 0.085, 0, new Point3D(0,1,0));
    private final Rotate upperArmShoulderJointTransformation = new Rotate(0,0, 0.151, -0.053, new Point3D(0,0,-1));
    private final Rotate shoulderForeArmJointTransformation = new Rotate(0,0, 0.39525, -0.06954, new Point3D(0,0,-1));
    private final Rotate foreArmWrist1JointTransformation = new Rotate(0,0, 0.609, -0.0609, new Point3D(0,0,-1));
    private final Rotate wrist1Wrist2JointTransformation = new Rotate(0,0, 0.64816, -0.1067, new Point3D(0,1,0));
    private final Rotate wrist2Wrist3JointTransformation = new Rotate(0,0, 0.69378, -0.14753, new Point3D(0,0,1));      //todo: axis impact

    private int stepsTaken = 0;

    private float incrementBy = 10.0f;

    private boolean jointAMovingPositive = true;
    private boolean jointBMovingPositive = true;
    private boolean jointCMovingPositive = true;
    private boolean jointDMovingPositive = true;
    private boolean jointEMovingPositive = true;

    private boolean stepFlip = false;

    public UR3Model() {
        loadParts();
        addTargetSphere();
    }

    private void loadParts() {
        try {

            URL base = getClass().getResource("/base.obj");
            URL shoulder = getClass().getResource("/shoulder.obj");
            URL upperArm = getClass().getResource("/upperarm.obj");
            URL foreArm = getClass().getResource("/forearm.obj");
            URL wrist1 = getClass().getResource("/wrist1.obj");
            URL wrist2 = getClass().getResource("/wrist2.obj");
            URL wrist3 = getClass().getResource("/wrist3.obj");

            URL table = getClass().getResource("/stul.obj");

            Affine3D shoulderAffine = new Affine3D();
            shoulderAffine.translate(0, 0.085, 0);

            Affine3D upperArmAffine = new Affine3D();
            upperArmAffine.translate(0, 0.151, -0.053);

            Affine3D foreArmAffine = new Affine3D();
            foreArmAffine.translate(0, 0.39525, -0.06954);

            Affine3D wrist1Affine = new Affine3D();
            wrist1Affine.translate(0, 0.609, -0.0609);

            Affine3D wrist2Affine = new Affine3D();
            wrist2Affine.translate(0, 0.64816, -0.1067);

            Affine3D wrist3Affine = new Affine3D();
            wrist3Affine.translate(0, 0.69378, -0.14753);


            PartArtifact robotTable = new PartArtifact(table,
                    "table", 0, true);

            PartArtifact robotBase = new PartArtifact(base,
                    "base", 1, true);
            PartArtifact robotShoulder = new PartArtifact(shoulder,
                 "shoulder", 3, true);
            PartArtifact robotUpperArm = new PartArtifact(upperArm,
               "upper_arm", 5, true);
            PartArtifact robotForeArm = new PartArtifact(foreArm,
                  "fore_arm", 7, true);
            PartArtifact robotWrist1 = new PartArtifact(wrist1,
                    "wrist_1", 9, true);
            PartArtifact robotWrist2 = new PartArtifact(wrist2,
                   "wrist_2", 11, true);
            PartArtifact robotWrist3 = new PartArtifact(wrist3,
                    "wrist_3", 13, true);


            JointArtifact baseShoulderJoint = new JointArtifact(baseShoulderJointTransformation,
                    "shoulderBaseJoint",
                    2, -90, 90, 10);
            JointArtifact shoulderUpperArmJoint = new JointArtifact(upperArmShoulderJointTransformation,
                    "shoulderUpperArmJoint",
                    4, -90, 90, 10);
            JointArtifact upperArmForeArmJoint = new JointArtifact(shoulderForeArmJointTransformation,
                    "upperArmForeArmJoint",
                    6,-90,90,0);
            JointArtifact foreArmWrist1Joint = new JointArtifact(foreArmWrist1JointTransformation,
                    "foreArmWrist1Joint",
                    8, -90, 90,0);
            JointArtifact wrist1Wrist2Joint = new JointArtifact(wrist1Wrist2JointTransformation,
                    "wrist1Wrist2Joint",
                    10, -90, 90,0);
            JointArtifact wrist2Wrist3Joint = new JointArtifact(wrist2Wrist3JointTransformation,
                    "wrist2Wrist3Joint",
                    12, -90, 90,0);

            //addToParts(robotTable);
            addToParts(robotBase);
            addToParts(baseShoulderJoint);

            robotShoulder.addOrSetTransform(baseShoulderJointTransformation);
            robotShoulder.addOrSetTransform(shoulderAffine.getTransform());

            addToParts(robotShoulder);
            addToParts(shoulderUpperArmJoint);

            robotUpperArm.addOrSetTransform(baseShoulderJointTransformation);
            robotUpperArm.addOrSetTransform(upperArmShoulderJointTransformation);
            robotUpperArm.addOrSetTransform(upperArmAffine.getTransform());


            addToParts(robotUpperArm);
            addToParts(upperArmForeArmJoint);

            robotForeArm.addOrSetTransform(baseShoulderJointTransformation);
            robotForeArm.addOrSetTransform(upperArmShoulderJointTransformation);
            robotForeArm.addOrSetTransform(shoulderForeArmJointTransformation);
            robotForeArm.addOrSetTransform(foreArmAffine.getTransform());

            addToParts(robotForeArm);
            addToParts(foreArmWrist1Joint);

            robotWrist1.addOrSetTransform(baseShoulderJointTransformation);
            robotWrist1.addOrSetTransform(upperArmShoulderJointTransformation);
            robotWrist1.addOrSetTransform(shoulderForeArmJointTransformation);
            robotWrist1.addOrSetTransform(foreArmWrist1JointTransformation);
            robotWrist1.addOrSetTransform(wrist1Affine.getTransform());

            addToParts(robotWrist1);
            addToParts(wrist1Wrist2Joint);

            robotWrist2.addOrSetTransform(baseShoulderJointTransformation);
            robotWrist2.addOrSetTransform(upperArmShoulderJointTransformation);
            robotWrist2.addOrSetTransform(shoulderForeArmJointTransformation);
            robotWrist2.addOrSetTransform(foreArmWrist1JointTransformation);
            robotWrist2.addOrSetTransform(wrist1Wrist2JointTransformation);
            robotWrist2.addOrSetTransform(wrist2Affine.getTransform());

            addToParts(robotWrist2);
            addToParts(wrist2Wrist3Joint);

            robotWrist3.addOrSetTransform(baseShoulderJointTransformation);
            robotWrist3.addOrSetTransform(upperArmShoulderJointTransformation);
            robotWrist3.addOrSetTransform(shoulderForeArmJointTransformation);
            robotWrist3.addOrSetTransform(foreArmWrist1JointTransformation);
            robotWrist3.addOrSetTransform(wrist1Wrist2JointTransformation);
            robotWrist3.addOrSetTransform(wrist2Wrist3JointTransformation);
            robotWrist3.addOrSetTransform(wrist3Affine.getTransform());

            addToParts(robotWrist3);


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void setBaseShoulderJointAngle(double angle) {
        baseShoulderJointTransformation.setAngle(angle);
    }

    void setUpperArmShoulderJointAngle(double angle) {
        upperArmShoulderJointTransformation.setAngle(angle);
    }

    void setShoulderForeArmJointAngle(double angle) {
        shoulderForeArmJointTransformation.setAngle(angle);
    }

    void setForeArmWrist1JointAngle(double angle) {
        foreArmWrist1JointTransformation.setAngle(angle);
    }

    void setWrist1Wrist2JointAngle(double angle) {
        wrist1Wrist2JointTransformation.setAngle(angle);
    }

    void setWrist2Wrist3JointAngle(double angle) {
        wrist2Wrist3JointTransformation.setAngle(angle);
    }

   synchronized public void incrementA() {
        setBaseShoulderJointAngle(baseShoulderJointTransformation.getAngle() + incrementBy);
        if(!jointAMovingPositive) {
            jointAMovingPositive = true;
            stepFlip = true;
        }
        stepsTaken++;
    }

    synchronized public void decrementA() {
        setBaseShoulderJointAngle(baseShoulderJointTransformation.getAngle() - incrementBy);

        if(jointAMovingPositive) {
            jointAMovingPositive = false;
            stepFlip = true;
        }
        stepsTaken++;
    }

    synchronized public void incrementB() {
        setUpperArmShoulderJointAngle(upperArmShoulderJointTransformation.getAngle() + incrementBy);
        if(!jointBMovingPositive) {
            jointBMovingPositive = true;
            stepFlip = true;
        }

        stepsTaken++;
    }

    synchronized public void decrementB() {
        setUpperArmShoulderJointAngle(upperArmShoulderJointTransformation.getAngle() - incrementBy);
        if(jointBMovingPositive) {
            jointBMovingPositive = false;
            stepFlip = true;
        }

        stepsTaken++;
    }

    synchronized  public void incrementC() {
        setShoulderForeArmJointAngle(baseShoulderJointTransformation.getAngle() + incrementBy);
        if(!jointCMovingPositive) {
            jointCMovingPositive = true;
            stepFlip = true;
        }

        stepsTaken++;
    }

    synchronized public void decrementC() {
        setShoulderForeArmJointAngle(baseShoulderJointTransformation.getAngle() - incrementBy);
        if(jointCMovingPositive) {
            jointCMovingPositive = false;
            stepFlip = true;
        }
        stepsTaken++;
    }
    synchronized public void incrementD() {
        setForeArmWrist1JointAngle(baseShoulderJointTransformation.getAngle() + incrementBy);
        if(!jointDMovingPositive) {
            jointDMovingPositive = true;
            stepFlip = true;
        }
        stepsTaken++;
    }

    synchronized public void decrementD() {
        setForeArmWrist1JointAngle(baseShoulderJointTransformation.getAngle() - incrementBy);
        if(jointDMovingPositive) {
            jointDMovingPositive = false;
            stepFlip = true;
        }
        stepsTaken++;
    }
    synchronized public void incrementE() {
        setWrist1Wrist2JointAngle(baseShoulderJointTransformation.getAngle() + incrementBy);
        if(!jointEMovingPositive) {
            jointEMovingPositive = true;
            stepFlip = true;
        }

        stepsTaken++;
    }

    synchronized public void decrementE() {
        setWrist1Wrist2JointAngle(baseShoulderJointTransformation.getAngle() - incrementBy);
        if(jointEMovingPositive) {
            jointEMovingPositive = false;
            stepFlip = true;
        }

        stepsTaken++;
    }

    synchronized public double getA() {
        return  baseShoulderJointTransformation.getAngle();
    }

    synchronized public double getB() {
        return  upperArmShoulderJointTransformation.getAngle();
    }

    synchronized public double getC() {
        return shoulderForeArmJointTransformation.getAngle();
    }

    synchronized public double getD() {
        return foreArmWrist1JointTransformation.getAngle();
    }

    synchronized public double getE() {
        return wrist1Wrist2JointTransformation.getAngle();
    }

    synchronized public int getStepsTaken() {
        return stepsTaken;
    }

    public void setJointAngles(double angleA, double angleB) {
        setBaseShoulderJointAngle(angleA);
        setBaseShoulderJointAngle(angleB);
    }

    synchronized public void reset() {
        setUpperArmShoulderJointAngle(0);
        setBaseShoulderJointAngle(0);
        setForeArmWrist1JointAngle(0);
        setShoulderForeArmJointAngle(0);
        setWrist1Wrist2JointAngle(0);
        setWrist2Wrist3JointAngle(0);
        stepsTaken = 0;
    }

    public boolean isStepFlip() {
        if(stepFlip){
            stepFlip = false;
            return true;
        }
        return stepFlip;
    }
}