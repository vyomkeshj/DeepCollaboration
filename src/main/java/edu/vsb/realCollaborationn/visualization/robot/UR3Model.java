package edu.vsb.realCollaborationn.visualization.robot;

import edu.vsb.realCollaborationn.visualization.shape3d.model.ArtifactStructure;
import edu.vsb.realCollaborationn.visualization.shape3d.model.JointArtifact;
import edu.vsb.realCollaborationn.visualization.shape3d.model.PartArtifact;
import edu.vsb.realCollaborationn.visualization.utils3d.geom.Vec3d;
import edu.vsb.realCollaborationn.visualization.utils3d.geom.transform.Affine3D;

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
            shoulderAffine.translate(0, 0.152, 0);

            Affine3D upperArmAffine = new Affine3D();
            upperArmAffine.translate(0, 0.152, -0.122);

            Affine3D foreArmAffine = new Affine3D();
            foreArmAffine.translate(0, 0.152+0.244, -0.029);

            Affine3D wrist1Affine = new Affine3D();
            wrist1Affine.translate(0, 0.152+0.244+0.213, -0.029);

            Affine3D wrist2Affine = new Affine3D();
            wrist2Affine.translate(0, 0.609, -0.115);

            Affine3D wrist3Affine = new Affine3D();
            wrist3Affine.translate(0, 0.609+0.080, -0.122);

            Vec3d baseShoulderJointAxis = new Vec3d(0,1,0);
            Vec3d baseShoulderJointPosition = new Vec3d(0,0,0);
            
            JointArtifact baseShoulderJoint = new JointArtifact(baseShoulderJointAxis,baseShoulderJointPosition, "shoulderBaseJoint",
                    2, -90, 90, 0);

            PartArtifact robotBase = new PartArtifact(base,
                    baseAffine, "base", 1, true);
            PartArtifact robotShoulder = new PartArtifact(shoulder,
                   shoulderAffine, "shoulder", 3, true);
            PartArtifact robotUpperArm = new PartArtifact(upperArm,
                    upperArmAffine, "upper_arm", 5, true);
            PartArtifact robotForeArm = new PartArtifact(foreArm,
                    foreArmAffine, "fore_arm", 7, true);
            PartArtifact robotWrist1 = new PartArtifact(wrist1,
                    wrist1Affine, "wrist_1", 9, true);
            PartArtifact robotWrist2 = new PartArtifact(wrist2,
                    wrist2Affine, "wrist_2", 11, true);
            PartArtifact robotWrist3 = new PartArtifact(wrist3,
                    wrist3Affine, "wrist_3", 13, true);



            addToParts(robotBase);
            addToParts(baseShoulderJoint);
            addToParts(robotShoulder);
            addToParts(robotUpperArm);
            addToParts(robotForeArm);
            addToParts(robotWrist1);
            addToParts(robotWrist2);
            addToParts(robotWrist3);

            rotateAtJoint(2,  1.54/2);


        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
