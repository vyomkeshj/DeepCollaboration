package edu.vsb.realCollaborationn.learning.model;

import edu.vsb.realCollaborationn.learning.PointProvider;
import edu.vsb.realCollaborationn.learning.actions.continuous.ContinuousAngleUpdateAction;
import edu.vsb.realCollaborationn.learning.methods.definitions.RobotAngleUpdate;
import edu.vsb.realCollaborationn.learning.model.baseActionSpace.ContinuousActionSpace;
import edu.vsb.realCollaborationn.visualization.robot.UR3Model;
import org.deeplearning4j.gym.StepReply;

public class RobotContinuousActionSpace extends ContinuousActionSpace {

    UR3Model ur3Model;
    PointProvider localPointProvider;
    ContinuousAngleUpdateAction updateAction;


    public RobotContinuousActionSpace(UR3Model robotModel, PointProvider pointProvider) {
        super(2);   //todo: make dynamic
        this.ur3Model = robotModel;
        this.localPointProvider = pointProvider;
        this.updateAction = new ContinuousAngleUpdateAction(robotModel, pointProvider);

    }

    public StepReply<Observation> executeAction(RobotAngleUpdate angleUpdate) {
       return updateAction.performAction(angleUpdate);
    }
}
