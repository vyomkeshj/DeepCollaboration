package edu.vsb.realCollaborationn.learning;

import edu.vsb.realCollaborationn.learning.implementations.URObservation;
import edu.vsb.realCollaborationn.learning.implementations.URControlActionSpace;
import edu.vsb.realCollaborationn.learning.model.Action;
import edu.vsb.realCollaborationn.learning.implementations.URSimRobotState;
import edu.vsb.realCollaborationn.visualization.robot.UR3Model;
import javafx.scene.Node;
import org.deeplearning4j.gym.StepReply;
import org.deeplearning4j.rl4j.mdp.MDP;
import org.deeplearning4j.rl4j.space.ObservationSpace;

public class BaseLearningEnvironment implements MDP{

    UR3Model currentRobotModel;
    URControlActionSpace actionSpace = new URControlActionSpace(currentRobotModel);
    URSimRobotState robotState = new URSimRobotState(currentRobotModel);
    URObservation observationSpace = new URObservation(robotState);

    public BaseLearningEnvironment() {
        this.currentRobotModel = new UR3Model();
    }

    @Override
    public ObservationSpace<URSimRobotState> getObservationSpace() {
        return observationSpace;
    }

    @Override
    public URControlActionSpace<Action> getActionSpace() {
        return actionSpace;
    }

    @Override
    public URSimRobotState reset() {
        currentRobotModel.rotateAtJoint(2,0);
        currentRobotModel.rotateAtJoint(4,0);
        return robotState;
    }

    @Override
    public void close() {

    }

    @Override
    public StepReply step(Object o) {
        if(o instanceof Action)
           ((Action) o).performAction();
        return observationSpace.getCurrentReward();
    }

    @Override
    public boolean isDone() {
        return false;
    }

    @Override
    public MDP newInstance() {
        return new BaseLearningEnvironment();
    }

    public Node getEnvironmentNodes() {
        return currentRobotModel.getStructureNode();
    }
}
