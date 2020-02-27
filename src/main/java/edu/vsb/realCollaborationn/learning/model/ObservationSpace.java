package edu.vsb.realCollaborationn.learning.model;

import edu.vsb.realCollaborationn.visualization.robot.UR3Model;
import javafx.geometry.Point3D;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.factory.Nd4j;

public class ObservationSpace implements org.deeplearning4j.rl4j.space.ObservationSpace<Observation> {

    private Observation robotObservation;

    public ObservationSpace(UR3Model robotModel, Point3D targetTCP) {
        robotObservation = new Observation(robotModel, targetTCP);
    }

    @Override
    public String getName() {
        return "Knock Knock";
    }

    @Override
    public int[] getShape() {
        return new int[] {12};
    }

    @Override
    public INDArray getLow() {
        double[] stateArray = {-1,-1,-1,-1,-1,0,0,0, 0, 0,0,0};
        return Nd4j.create(stateArray);
    }

    @Override
    public INDArray getHigh() {
        int[] stateArray = {1,1,1,1,1,1,1,1,1,1,1,1};
        return Nd4j.create(stateArray);
    }

    public INDArray getObservation() {
        return robotObservation.getData();
    }
}
