package edu.vsb.realCollaborationn.learning.actions;

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
        Observation currentObservation = new Observation(currentModel);
        StepReply<Observation> reply = new StepReply<Observation>(currentObservation, currentObservation.getReward(targetPoint), false, new JSONObject(currentObservation));
        return reply;
    }
}
