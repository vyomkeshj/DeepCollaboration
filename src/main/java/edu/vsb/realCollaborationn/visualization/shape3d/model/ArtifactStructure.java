package edu.vsb.realCollaborationn.visualization.shape3d.model;

import edu.vsb.realCollaborationn.visualization.utils3d.geom.Vec3d;
import edu.vsb.realCollaborationn.visualization.utils3d.geom.transform.Affine3D;
import javafx.geometry.Point3D;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;

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

                if(artifact.getArtifactKey().equals("upper_arm") || artifact.getArtifactKey().equals("base")) {
                    Affine3D artifactAffine = artifact.getTransform();

                    Vec3d rotatorJointTranslation = transformerJoint.getJointTranslation();
                    Vec3d rotatorJointAxis = transformerJoint.getJointAxis();


                    Point3D rotationAxis = new Point3D(rotatorJointAxis.x, rotatorJointAxis.y, rotatorJointAxis.z);
                    Rotate rotationTrf = new Rotate(angle, artifactAffine.getMxt(), artifactAffine.getMyt(), artifactAffine.getMzt(), rotationAxis);

                    System.out.println("part = " + artifact.getArtifactKey() + " Transformer= " + rotationTrf);
                    artifactAffine.preTransform(new Affine3D(rotationTrf));

                    artifact.setTransform(new Affine3D(rotationTrf));
                }
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
