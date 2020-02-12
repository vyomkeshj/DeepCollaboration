package edu.vsb.realCollaborationn.visualization.shape3d.model;

import edu.vsb.realCollaborationn.visualization.utils3d.geom.Vec3d;
import edu.vsb.realCollaborationn.visualization.utils3d.geom.transform.Affine3D;
import javafx.scene.Group;
import javafx.scene.Node;

import java.util.ArrayList;
import java.util.List;

public class ArtifactStructure {
    private List<Artifact> partList = new ArrayList<>();

    public void addToParts(Artifact toAddArtifact) {
        partList.add(toAddArtifact);
    }

    public List<PartArtifact> getPartsList() {
        List<PartArtifact> partsList = new ArrayList<>();
        for (Artifact currentPart: partList) {
            if(currentPart instanceof PartArtifact) {
                partsList.add((PartArtifact) currentPart); //todo: check if it works
            }
        }
        return partsList;
    }

    public List<JointArtifact> getJointsList() {
        List<JointArtifact> jointsList = new ArrayList<>();
        for (Artifact currentPart: partList) {
            if(currentPart instanceof JointArtifact) {
                jointsList.add((JointArtifact) currentPart); //todo: check if it works
            }
        }
        return jointsList;
    }
    //fixme: use subsequent artifact's translation
    public boolean rotateAtJoint(int jointIndex, double angle) {
        boolean transformFound = false;
        JointArtifact transformerJoint = null;      //to be used to compute the transformation of subsequent parts
        for(Artifact artifact: partList) {
            if (artifact instanceof JointArtifact && !transformFound) {
                JointArtifact currentArtifact = (JointArtifact) artifact;
                if (currentArtifact.getArtifactPosition() == jointIndex) {
                    transformFound = true;
                    transformerJoint = currentArtifact;
                }
            } else if (transformFound){
                //todo: pretranslate, rotate, retranslate

                Affine3D  artifactAffine  = artifact.getTransform();

                Vec3d rotatorJointTranslation = transformerJoint.getJointTranslation();
                Vec3d rotatorJointAxis = transformerJoint.getJointAxis();

                System.out.println("artifact's initial: "+artifactAffine);

                Affine3D preTranslation = new Affine3D();
                        preTranslation.translate(-rotatorJointTranslation.x,
                        -rotatorJointTranslation.y, -rotatorJointTranslation.z);    //fixme: is translate = setting translation

                System.out.println("artifact's pretranslate: "+preTranslation);

                Affine3D rotation = new Affine3D();
                rotation.setToRotation(angle, rotatorJointAxis.x, rotatorJointAxis.y, rotatorJointAxis.z
                , rotatorJointTranslation.x, rotatorJointTranslation.y, rotatorJointTranslation.z);

                System.out.println("artifact's rotation: "+rotation);

                Affine3D postTranslation = new Affine3D();
                postTranslation.translate(+rotatorJointTranslation.x,
                        +rotatorJointTranslation.y, +rotatorJointTranslation.z);
                System.out.println("artifact's post translation: "+postTranslation);

                //artifactAffine.preTransform(preTranslation);
                artifactAffine.preTransform(rotation);
                artifactAffine.preTransform(rotation);

                // artifactAffine.preTransform(postTranslation);
                artifact.setTransform(artifactAffine);
            }
        }
        return false;
    }

    public Node getStructureNode() {
        Group rootNode = new Group();
        for (Artifact currentPart: partList) {
            if(currentPart instanceof PartArtifact) {
                rootNode.getChildren().add(((PartArtifact) currentPart).artifactModel);
            }
        }
        return rootNode;
    }
}
