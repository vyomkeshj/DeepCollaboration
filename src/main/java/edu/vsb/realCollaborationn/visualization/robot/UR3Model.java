package edu.vsb.realCollaborationn.visualization.robot;

import edu.vsb.realCollaborationn.visualization.shape3d.model.ArtifactStructure;
import edu.vsb.realCollaborationn.visualization.shape3d.model.JointArtifact;
import edu.vsb.realCollaborationn.visualization.shape3d.model.PartArtifact;
import edu.vsb.realCollaborationn.visualization.utils3d.geom.Vec3d;
import edu.vsb.realCollaborationn.visualization.utils3d.geom.transform.Affine3D;
import javafx.geometry.Point3D;
import javafx.scene.transform.Rotate;

import java.io.IOException;
import java.net.URL;

public class UR3Model extends ArtifactStructure {

    private static final double PI = 3.14;

    public UR3Model() {
        loadParts();
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


            Affine3D baseAffine = new Affine3D();

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

            Vec3d baseShoulderJointAxis = new Vec3d(0,1,0);
            Vec3d baseShoulderJointPosition = new Vec3d(0,0.085,0);


            Rotate baseShoulderJointTransformation = new Rotate(0,0, 0.085, 0, new Point3D(0,1,0));
            JointArtifact baseShoulderJoint = new JointArtifact(baseShoulderJointTransformation,"shoulderBaseJoint", 2, -90, 90, 10);

            Rotate upperarmShoulderJointRot = new Rotate(0,0, 0.151, -0.053, new Point3D(0,0,-1));
            JointArtifact shoulderUpperArmJoint = new JointArtifact(upperarmShoulderJointRot,"shoulderUpperArmJoint", 4, -90, 90, 10);



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



            addToParts(robotBase);

            addToParts(baseShoulderJoint);
            //baseShoulderJoint.addTransform(baseShoulderJointTransformation);

            robotShoulder.addTransform(baseShoulderJointTransformation);
            robotShoulder.addTransform(shoulderAffine.getTransform());

            addToParts(robotShoulder);

            addToParts(shoulderUpperArmJoint);

            robotUpperArm.addTransform(baseShoulderJointTransformation);
            robotUpperArm.addTransform(upperarmShoulderJointRot);

            robotUpperArm.addTransform(upperArmAffine.getTransform());
            addToParts(robotUpperArm);


            robotForeArm.addTransform(baseShoulderJointTransformation);
            robotForeArm.addTransform(upperarmShoulderJointRot);

            robotForeArm.addTransform(foreArmAffine.getTransform());
            addToParts(robotForeArm);


//            addToParts(robotWrist1);
//            robotWrist1.addTransform(baseShoulderJointTransformation);
//            addToParts(robotWrist2);
//            robotWrist2.addTransform(baseShoulderJointTransformation);
//            addToParts(robotWrist3);
//            robotWrist3.addTransform(baseShoulderJointTransformation);


            baseShoulderJointTransformation.setAngle(60);
            baseShoulderJointTransformation.setAngle(40);
            upperarmShoulderJointRot.setAngle(40);
            //rotateAtJoint(4,  45);
            //rotateAtJoint(2,  -45);


        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
