package edu.vsb.realCollaborationn.learning;

import edu.vsb.realCollaborationn.learning.methods.definitions.RobotAngleUpdate;
import edu.vsb.realCollaborationn.learning.model.Observation;
import edu.vsb.realCollaborationn.learning.model.ObservationSpace;
import edu.vsb.realCollaborationn.learning.model.RobotContinuousActionSpace;
import edu.vsb.realCollaborationn.learning.model.baseActionSpace.ContinuousActionSpace;
import edu.vsb.realCollaborationn.visualization.robot.UR3Model;
import javafx.geometry.Point3D;
import org.deeplearning4j.gym.StepReply;
import org.deeplearning4j.rl4j.mdp.MDP;

public class RobotDecisionProcessContinuous implements MDP<Observation, RobotAngleUpdate, ContinuousActionSpace> {

    public static int THREAD_NO = 0;

    UR3Model robotModel;
    edu.vsb.realCollaborationn.learning.model.ObservationSpace currentObservationSpace;
    RobotContinuousActionSpace robotContinuousActionSpace;
    ContinuousActionSpace currentActionSpace = new ContinuousActionSpace(2);    //todo: make dependent
    PointProvider targetProvider = new PointProvider();
    private boolean isDone = false;


    public RobotDecisionProcessContinuous() {
        this.robotModel = new UR3Model();
        currentObservationSpace = new ObservationSpace(robotModel, targetProvider.renewPointTarget());
        robotContinuousActionSpace = new RobotContinuousActionSpace(robotModel, targetProvider);
    }

    public void setCurrentPointTargetForTCP(Point3D currentPointTargetForTCP) {
        robotModel.translateTargetSphere(currentPointTargetForTCP);
    }

    @Override
    public org.deeplearning4j.rl4j.space.ObservationSpace<Observation> getObservationSpace() {
        return currentObservationSpace;
    }

    @Override
    public ContinuousActionSpace getActionSpace() {
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
    public StepReply<Observation> step(RobotAngleUpdate angleUpdate) {

        StepReply<Observation> currentStepReply = robotContinuousActionSpace.executeAction(angleUpdate);
        isDone = currentStepReply.isDone();
        return currentStepReply;
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
    public MDP<Observation, RobotAngleUpdate, ContinuousActionSpace> newInstance() {
        return new RobotDecisionProcessContinuous();
    }

}