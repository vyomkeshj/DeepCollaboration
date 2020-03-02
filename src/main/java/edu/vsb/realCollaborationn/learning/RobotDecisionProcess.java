package edu.vsb.realCollaborationn.learning;

import edu.vsb.realCollaborationn.learning.model.*;
import edu.vsb.realCollaborationn.visualization.robot.UR3Model;
import javafx.geometry.Point3D;
import org.deeplearning4j.gym.StepReply;
import org.deeplearning4j.rl4j.mdp.MDP;

import static edu.vsb.realCollaborationn.learning.Utils.getRandomPointsBetweenTwoConcentricSpheres;

public class RobotDecisionProcess implements MDP<Observation, Integer, DiscreteActionSpace> {

    UR3Model robotModel;
    ObservationSpace currentObservationSpace;
    ActionSpace actionSpace;
    DiscreteActionSpace currentActionSpace = new DiscreteActionSpace(11);
    private boolean isDone = false;

    public static Point3D currentFixedPointTarget = new Point3D(0.1496433,0.278275, 0.1997760);
    public RobotDecisionProcess(UR3Model robotModel) {
        this.robotModel = robotModel;
        currentObservationSpace = new ObservationSpace(robotModel, currentFixedPointTarget);
        actionSpace = new ActionSpace(robotModel);
    }

    public RobotDecisionProcess() {
        this.robotModel = new UR3Model();
        currentObservationSpace = new ObservationSpace(robotModel, currentFixedPointTarget);
        actionSpace = new ActionSpace(robotModel);
    }

    public void setCurrentPointTargetForTCP(Point3D currentPointTargetForTCP) {
        this.currentFixedPointTarget = currentPointTargetForTCP;
        robotModel.translateTargetSphere(currentPointTargetForTCP);
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
        robotModel.reset();
        setCurrentPointTargetForTCP(currentFixedPointTarget);
        return new Observation(robotModel);
    }

    @Override
    public void close() {
        System.out.println("CLOSE CALLED");
    }

    @Override
    public StepReply<Observation> step(Integer integer) {
        StepReply<Observation> currentStepReply = actionSpace.executeActionAt(integer);
        isDone = currentStepReply.isDone();
        return currentStepReply;
    }

    @Override
    public boolean isDone() {
         if(isDone) {
             isDone = false;
             return true;
         }
         return false;
    }

    @Override
    public MDP<Observation, Integer, DiscreteActionSpace> newInstance() {
        return new RobotDecisionProcess();
    }

}
