package edu.vsb.realCollaborationn.learning.actions;

import edu.vsb.realCollaborationn.learning.Utils;
import edu.vsb.realCollaborationn.learning.model.Action;
import edu.vsb.realCollaborationn.learning.model.Observation;
import edu.vsb.realCollaborationn.visualization.robot.UR3Model;
import javafx.geometry.Point3D;
import org.deeplearning4j.gym.StepReply;
import org.json.JSONObject;


public class NoOpAction implements Action {

    UR3Model currentModel;
    Point3D targetPoint;

    public NoOpAction(UR3Model currentModel, Point3D targetPoint) {
        this.currentModel = currentModel;
        this.targetPoint = targetPoint;
    }

    @Override
    public StepReply<Observation> performAction() {
        //System.out.println("NoOp");

        Observation currentObservation = new Observation(currentModel, targetPoint);
        double reward = currentObservation.getReward(targetPoint);
        reward-=2d;
        double distanceFromTarget = currentObservation.getDistanceFromTarget(targetPoint);
        boolean isDone = (distanceFromTarget<MAX_REWARD);
        if(isDone) {
            System.out.println("___________DONE_______NP_____");
            reward = reward+10;
            Utils.MADE_IT_TO_TARGET = true;
            targetPoint = Utils.getTargetOnConstrainedRobot();

        }
        StepReply<Observation> reply = new StepReply<Observation>(currentObservation, reward, false, new JSONObject(currentObservation));
        return reply;
    }

    @Override
    public void setTarget(Point3D target) {
        this.targetPoint = target;
    }

    @Override
    public Integer getEncoding() {
        return 11;
    }
}
