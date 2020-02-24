package edu.vsb.realCollaborationn.learning;

import edu.vsb.realCollaborationn.learning.model.*;
import edu.vsb.realCollaborationn.visualization.robot.UR3Model;
import javafx.geometry.Point3D;
import org.deeplearning4j.gym.StepReply;
import org.deeplearning4j.rl4j.mdp.MDP;

public class RobotDecisionProcess implements MDP<Observation, Integer, DiscreteActionSpace> {

    UR3Model robotModel;
    ObservationSpace currentObservationSpace;
    ActionSpace actionSpace;
    DiscreteActionSpace currentActionSpace = new DiscreteActionSpace(5);

    Point3D currentPointTargetForTCP = new Point3D(0,0,0);

    public RobotDecisionProcess(UR3Model robotModel) {
        this.robotModel = robotModel;
        currentObservationSpace = new ObservationSpace(robotModel);
        actionSpace = new ActionSpace(robotModel);
    }

    public RobotDecisionProcess() {
        this.robotModel = new UR3Model();
    }

    public void setCurrentPointTargetForTCP(Point3D currentPointTargetForTCP) {
        this.currentPointTargetForTCP = currentPointTargetForTCP;
        actionSpace.setTargetPoint(currentPointTargetForTCP);            //sets the target point that the robot has to reach
    }

    @Override
    public org.deeplearning4j.rl4j.space.ObservationSpace<Observation> getObservationSpace() {
        return currentObservationSpace;
    }

    @Override
    public DiscreteActionSpace getActionSpace() {
        return currentActionSpace;
    }

    @Override
    public Observation reset() {
        return new Observation(robotModel);
    }

    @Override
    public void close() {

    }

    @Override
    public StepReply<Observation> step(Integer integer) {
        return actionSpace.executeActionAt(integer);
    }

    @Override
    public boolean isDone() {
        return false;
    }

    @Override
    public MDP<Observation, Integer, DiscreteActionSpace> newInstance() {
        return new RobotDecisionProcess();
    }

}
