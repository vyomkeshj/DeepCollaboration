package edu.vsb.realCollaborationn.learning.actions;

import edu.vsb.realCollaborationn.learning.model.Action;
import edu.vsb.realCollaborationn.learning.model.Observation;
import edu.vsb.realCollaborationn.visualization.robot.UR3Model;
import javafx.geometry.Point3D;
import org.deeplearning4j.gym.StepReply;
import org.json.JSONObject;

public class JointDMovePosAction implements Action {

    UR3Model currentModel;
    Point3D targetPoint;
    public JointDMovePosAction(UR3Model currentModel, Point3D targetPoint) {
        this.currentModel = currentModel;
        this.targetPoint = targetPoint;
    }

    @Override
    public StepReply<Observation> performAction() {
        currentModel.incrementD();
        Observation currentObservation = new Observation(currentModel);
        double reward = currentObservation.getReward(targetPoint);
        boolean isDone = (reward>MAX_REWARD);
        StepReply<Observation> reply = new StepReply<Observation>(currentObservation, reward, isDone, new JSONObject(currentObservation));
        return reply;
    }
}
