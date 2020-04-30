package edu.vsb.realCollaborationn.learning;

import edu.vsb.realCollaborationn.learning.model.*;
import edu.vsb.realCollaborationn.visualization.robot.UR3Model;
import javafx.geometry.Point3D;
import org.deeplearning4j.gym.StepReply;
import org.deeplearning4j.rl4j.mdp.MDP;

import static edu.vsb.realCollaborationn.learning.Utils.getRandomPointsBetweenTwoConcentricSpheres;

public class RobotDecisionProcess implements MDP<Observation, Integer, DiscreteActionSpace> {
    public static int THREAD_NO = 0;

    UR3Model robotModel;
    ObservationSpace currentObservationSpace;
    ActionSpace actionSpace;
    DiscreteActionSpace currentActionSpace = new DiscreteActionSpace(5);    //todo: make dependent
    PointProvider targetProvider = new PointProvider();
    private boolean isDone = false;


    public RobotDecisionProcess(UR3Model robotModel) {
        Thread.currentThread().setName("Th"+THREAD_NO);
        THREAD_NO++;

        this.robotModel = robotModel;
        currentObservationSpace = new ObservationSpace(robotModel, targetProvider.renewPointTarget());
        actionSpace = new ActionSpace(robotModel, targetProvider);
    }

    public RobotDecisionProcess() {
        Thread.currentThread().setName("Th"+THREAD_NO);
        THREAD_NO++;

        this.robotModel = new UR3Model();
        currentObservationSpace = new ObservationSpace(robotModel, targetProvider.renewPointTarget());
        actionSpace = new ActionSpace(robotModel, targetProvider);
    }

    public void setCurrentPointTargetForTCP(Point3D currentPointTargetForTCP) {
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
        setCurrentPointTargetForTCP(targetProvider.renewPointTarget());
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

    public StepReply<Observation> step(double angleA, double angleB) {
    return actionSpace.executeContinuousAction(angleA, angleB);
    }

    @Override
    public boolean isDone() {
         if(isDone) {
             isDone = false;
             reset();
             return true;
         }
         return false;
    }

    @Override
    public MDP<Observation, Integer, DiscreteActionSpace> newInstance() {
        return new RobotDecisionProcess();
    }

}
