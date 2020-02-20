package edu.vsb.realCollaborationn.learning.implementations;

import org.deeplearning4j.gym.StepReply;
import org.deeplearning4j.rl4j.space.ObservationSpace;
import org.json.JSONObject;
import org.nd4j.linalg.api.ndarray.INDArray;

public class URObservation implements ObservationSpace<URSimRobotState> {

    URSimRobotState robotState;

    public URObservation(URSimRobotState robotState) {
        this.robotState = robotState;
    }

    public StepReply<URSimRobotState> getCurrentReward() {
        return new StepReply<>(robotState, robotState.getReward(),false,new JSONObject());
    }

    @Override
    public String getName() {
        return "obs1";
    }

    @Override
    public int[] getShape() {
        return new int[0];
    }

    @Override
    public INDArray getLow() {
        return null;
    }

    @Override
    public INDArray getHigh() {
        return null;
    }
}
