package edu.vsb.realCollaborationn.visualization.robot;

import edu.vsb.realCollaborationn.visualization.shape3d.model.ArtifactStructure;
import edu.vsb.realCollaborationn.visualization.shape3d.model.PartArtifact;
import edu.vsb.realCollaborationn.visualization.utils3d.geom.transform.Affine3D;

import java.io.IOException;
import java.net.URL;

public class UR3Model extends ArtifactStructure {

    private static final double SHOULDER_HEIGHT = 0.1519;
    private static final double UPPER_ARM_LENGTH = 0.24365;
    private static final double FOREARM_LENGTH = 0.21325;
    private static final double WRIST_1_LENGTH = 0.11235 +0.0925 -0.1198;
    private static final double WRIST_2_LENGTH = 0.08535;
    private static final double WRIST_3_LENGTH = 0.0819;


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

            PartArtifact robotBase = new PartArtifact(base,
                    baseAffine, "base", 1, true);
            PartArtifact robotShoulder = new PartArtifact(shoulder,
                   shoulderAffine, "shoulder", 3, true);
            PartArtifact robotUpperArm = new PartArtifact(upperArm,
                    upperArmAffine, "uppper_arm", 5, true);
            PartArtifact robotForeArm = new PartArtifact(foreArm,
                    foreArmAffine, "fore_arm", 7, true);
            PartArtifact robotWrist1 = new PartArtifact(wrist1,
                    wrist1Affine, "wrist_1", 9, true);
            PartArtifact robotWrist2 = new PartArtifact(wrist2,
                    wrist2Affine, "wrist_2", 11, true);
            PartArtifact robotWrist3 = new PartArtifact(wrist3,
                    wrist3Affine, "wrist_3", 13, true);


            addToParts(robotBase);
            addToParts(robotShoulder);
            addToParts(robotUpperArm);
            addToParts(robotForeArm);
            addToParts(robotWrist1);
            addToParts(robotWrist2);
            addToParts(robotWrist3);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
