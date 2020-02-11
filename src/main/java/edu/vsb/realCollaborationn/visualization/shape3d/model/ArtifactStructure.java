package edu.vsb.realCollaborationn.visualization.shape3d.model;

import edu.vsb.realCollaborationn.visualization.utils3d.geom.Vec3d;
import edu.vsb.realCollaborationn.visualization.utils3d.geom.transform.Affine3D;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.transform.Affine;

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
        Affine3D subsequentTransform = null;
        JointArtifact transformerJoint = null;
        for(Artifact artifact: partList) {
            if (artifact instanceof JointArtifact && !transformFound) {
                JointArtifact currentArtifact = (JointArtifact) artifact;
                if (currentArtifact.getArtifactPosition() == jointIndex) {
                    transformFound = true;
                    transformerJoint = currentArtifact;
                    subsequentTransform = currentArtifact.getTransformerForRotation(angle);
                }
            } else if (transformFound){
                //todo: pretranslate, rotate, retranslate
                Affine3D  artifactAffine  = artifact.getTransform();
                Vec3d transformerAxis  = transformerJoint.getJointAxis();
                Vec3d transformerTranslation = transformerJoint.getJointTranslation();

                Affine3D preTranslation = artifact.getTransform();
                System.out.println(artifact.getArtifactKey()+ "before : "+preTranslation);

                preTranslation.preTranslate(0, -0.152, 1);
                Affine3D affineTransformer = new Affine3D();
                affineTransformer.translate(1.2, 1.4,1.3);
                preTranslation.preTransform(new Affine3D());

                //preTranslation.translate(0, -0.152, 1);
                //preTranslation.rotate(angle, transformerAxis.x, transformerAxis.y, transformerAxis.z);
                //preTranslation.translate(artifactAffine.getMxt(), 0.152, artifactAffine.getMzt());
//
//                Affine3D postTranslation = artifact.getTransform();
//                preTranslation.translate(transformerTranslation.x, transformerTranslation.y, transformerTranslation.z);
//
//
//                Affine3D transform = new Affine3D();
//                transform.setToRotation(angle, transformerAxis.x, transformerAxis.y, transformerAxis.z);
//                System.out.println(transform);
//
//                transform.preTransform(preTranslation);
               // postTranslation.preTransform(transform);
                System.out.println("artifactName: "+artifact.getArtifactKey());
                System.out.println("artifactAffine: "+artifactAffine);
                System.out.println("transform: "+preTranslation);
                artifact.transform(preTranslation);
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
