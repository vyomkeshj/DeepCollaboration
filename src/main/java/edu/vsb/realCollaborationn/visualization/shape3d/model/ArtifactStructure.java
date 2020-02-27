package edu.vsb.realCollaborationn.visualization.shape3d.model;

import javafx.geometry.Point3D;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.shape.Sphere;
import javafx.scene.transform.Transform;
import javafx.scene.transform.Translate;

import java.util.ArrayList;
import java.util.List;

public class ArtifactStructure {
    private List<Artifact> partList = new ArrayList<>();
    private Group rootNode = new Group();
    private Sphere targetSphere = new Sphere(0.02);;
    private Translate sphereTranslator = new Translate(0,0,0);
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

    public void rotateAtJoint(int jointIndex, double angle) {//todo: throw exception when joint doesn't exist
        for(Artifact artifact: partList) {
            if (artifact instanceof JointArtifact) {
                JointArtifact currentArtifact = (JointArtifact) artifact;
                if (currentArtifact.getArtifactPositionInPartsSequence() == jointIndex) {
                    currentArtifact.getJointAxis().setAngle(angle);
                }
            }
        }

        System.out.println("TCP at: "+getTCPcoords());
    }

    public void transformStructureRoot(Transform structureTransformer) {
     rootNode.getTransforms().add(structureTransformer);
    }


    public Point3D getTCPcoords() {
        Artifact lastPart = partList.get(partList.size()-1);
        Transform partTransform = lastPart.getTransform();
        return new Point3D(partTransform.getTx(), partTransform.getTy(), partTransform.getTz());
    }

    public Node getStructureNode() {
        for (Artifact currentPart: partList) {
            if(currentPart instanceof PartArtifact) {
                rootNode.getChildren().add(((PartArtifact) currentPart).getArtifactModel());
            }
        }
        return rootNode;
    }

    public void addTargetSphere() {
        rootNode.getChildren().add(targetSphere);
    }

    synchronized public void translateTargetSphere(Point3D translation) {
        targetSphere.setTranslateY(translation.getY());
        targetSphere.setTranslateX(translation.getX());
        targetSphere.setTranslateZ(translation.getZ());
    }
}
