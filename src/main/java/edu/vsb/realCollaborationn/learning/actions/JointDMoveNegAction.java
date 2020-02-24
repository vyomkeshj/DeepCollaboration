package edu.vsb.realCollaborationn.learning.actions;

import edu.vsb.realCollaborationn.learning.model.Action;
import edu.vsb.realCollaborationn.learning.model.Observation;
import edu.vsb.realCollaborationn.visualization.robot.UR3Model;
import javafx.geometry.Point3D;
import org.deeplearning4j.gym.StepReply;
import org.json.JSONObject;

public class JointDMoveNegAction implements Action {
    UR3Model currentModel;
    Point3D targetPoint;
    public JointDMoveNegAction(UR3Model currentModel, Point3D targetPoint) {
        this.currentModel = currentModel;
        this.targetPoint = targetPoint;
    }

    @Override
    public StepReply<Observation> performAction() {
        currentModel.decrementD();
        Observation currentObservation = new Observation(currentModel);
        double reward = currentObservation.getReward(targetPoint);
        boolean isDone = (reward>MAX_REWARD);
        if(isDone) {
            System.out.println("___________DONE____________");
        }
        StepReply<Observation> reply = new StepReply<Observation>(currentObservation, reward, isDone, new JSONObject(currentObservation));
        return reply;
    }
}
