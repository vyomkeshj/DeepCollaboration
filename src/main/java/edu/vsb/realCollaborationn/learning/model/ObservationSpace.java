package edu.vsb.realCollaborationn.learning.model;

import edu.vsb.realCollaborationn.visualization.robot.UR3Model;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.factory.Nd4j;

public class ObservationSpace implements org.deeplearning4j.rl4j.space.ObservationSpace<Observation> {

    private Observation robotObservation;

    public ObservationSpace(UR3Model robotModel) {
        robotObservation = new Observation(robotModel);
    }

    @Override
    public String getName() {
        return "Knock Knock";
    }

    @Override
    public int[] getShape() {
        return new int[] {5};
    }

    @Override
    public INDArray getLow() {
        int[] stateArray = {(int) 0, (int) 0, (int) 15, (int) 60, (int) 20};
        return Nd4j.create(stateArray);
    }

    @Override
    public INDArray getHigh() {
        int[] stateArray = {(int) 180, (int) 180, (int) 150, (int) 600, (int) 200};
        return Nd4j.create(stateArray);
    }

    public INDArray getObservation() {
        return robotObservation.getData();
    }
}
