package edu.vsb.realCollaborationn.learning.actions;

import edu.vsb.realCollaborationn.learning.model.Action;
import edu.vsb.realCollaborationn.learning.model.Observation;
import edu.vsb.realCollaborationn.visualization.robot.UR3Model;
import javafx.geometry.Point3D;
import org.deeplearning4j.gym.StepReply;
import org.json.JSONObject;

public class JointEMoveNegAction implements Action {
    UR3Model currentModel;
    Point3D targetPoint;
    public JointEMoveNegAction(UR3Model currentModel, Point3D targetPoint) {
        this.currentModel = currentModel;
        this.targetPoint = targetPoint;
    }

    @Override
    public StepReply<Observation> performAction() {
        currentModel.decrementE();
        Observation currentObservation = new Observation(currentModel);
        double reward = currentObservation.getReward(targetPoint);
        double distanceFromTarget = currentObservation.getDistanceFromTarget(targetPoint);
        boolean isDone = (distanceFromTarget<MAX_REWARD);
        if(isDone) {
            System.out.println("___________DONE____________");
            reward = reward+10;
        }
        StepReply<Observation> reply = new StepReply<Observation>(currentObservation, reward, false, new JSONObject(currentObservation));
        return reply;
    }

    @Override
    public void setTarget(Point3D target) {
        this.targetPoint = target;
    }
}