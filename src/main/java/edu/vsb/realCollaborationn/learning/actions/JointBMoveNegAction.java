package edu.vsb.realCollaborationn.learning.actions;

import edu.vsb.realCollaborationn.learning.Utils;
import edu.vsb.realCollaborationn.learning.model.Action;
import edu.vsb.realCollaborationn.learning.model.Observation;
import edu.vsb.realCollaborationn.visualization.robot.UR3Model;
import javafx.geometry.Point3D;
import org.deeplearning4j.gym.StepReply;
import org.json.JSONObject;

import static edu.vsb.realCollaborationn.learning.Utils.MADE_IT_TO_TARGET;

public class JointBMoveNegAction implements Action {
    UR3Model currentModel;
    Point3D targetPoint;

    public JointBMoveNegAction(UR3Model currentModel, Point3D targetPoint) {
        this.currentModel = currentModel;
        this.targetPoint = targetPoint;
    }

    @Override
    public StepReply<Observation> performAction() {
        if(MADE_IT_TO_TARGET)
            this.targetPoint = Utils.getTargetOnConstrainedRobot();

        currentModel.decrementB();
        Observation currentObservation = new Observation(currentModel, targetPoint);
        double reward = currentObservation.getReward(targetPoint);
        double distanceFromTarget = currentObservation.getDistanceFromTarget(targetPoint);
        boolean isDone = (distanceFromTarget<MAX_REWARD);
        if(isDone) {
            System.out.println("___________DONE____________");
            reward = reward+10;
            MADE_IT_TO_TARGET = true;

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
        return 3;
    }
}
